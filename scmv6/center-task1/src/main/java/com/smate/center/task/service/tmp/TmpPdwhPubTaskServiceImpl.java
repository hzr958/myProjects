package com.smate.center.task.service.tmp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubKeywordsDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.data.XmlUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class TmpPdwhPubTaskServiceImpl implements TmpPdwhPubTaskService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.UpdatePdwhPubKeywordsTask;
  @Autowired
  private PdwhPubXmlDao pubXmlDao;
  @Autowired
  private PdwhPubKeywordsDao pdwhPubKeywordsDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void startUpdateKeywords(TmpTaskInfoRecord job) {
    PdwhPubXml pdwhPubXml = pubXmlDao.getpdwhPubXmlPubId(job.getHandleId());
    if (pdwhPubXml == null || StringUtils.isBlank(pdwhPubXml.getXml())) {
      this.updateTaskStatus(job.getJobId(), 3, "成果xml数据为空");
      return;
    }
    try {
      ImportPubXmlDocument document = new ImportPubXmlDocument(pdwhPubXml.getXml());
      String zhKeywords = XmlUtil.subStr500char(document.getZhKeywords());
      String enKeywords = XmlUtil.subStr500char(document.getEnKeywords());
      String authorNames = XmlUtil.cleanAuthorsAddr(XmlUtil.subStr500char(document.getAuthorNames()));
      String authorNameSpec = XmlUtil.cleanAuthorsAddr(XmlUtil.subStr500char(document.getAuthorNameSpec()));
      String zhTitle = StringUtils.substring((String) document.getZhTitle(), 0, 500);
      String enTitle = StringUtils.substring((String) document.getEnTitle(), 0, 500);

      if (StringUtils.isBlank(zhKeywords) && StringUtils.isBlank(enKeywords) && StringUtils.isBlank(authorNames)
          && StringUtils.isBlank(authorNameSpec) && StringUtils.isBlank(zhTitle) && StringUtils.isBlank(enTitle)) {
        this.updateTaskStatus(job.getJobId(), 3, "关键词、标题、作者信息都为空");
        return;
      }
      // Map<String, String> authorMap = swapAuthorNames(authorNames,
      // authorNameSpec);

      PdwhPublication pdwhPublication = pdwhPublicationDao.get(job.getHandleId());

      if (pdwhPublication == null) {
        logger.error("Pdwh_Publication表没有该成果信息，pubId={}", job.getHandleId());
        this.updateTaskStatus(job.getJobId(), 3, "PDWH_PUBLICATION表没有该成果信息");
        return;
      }

      pdwhPublication.setZhTitle(HtmlUtils.Html2Text(zhTitle));// 清理 html
      // css
      // js
      pdwhPublication.setEnTitle(HtmlUtils.Html2Text(enTitle));

      // String zhAuthorNames = authorMap.get("zhAuthorNames");
      pdwhPublication.setAuthorName(XmlUtil.formatPubAuthorKws(authorNames));
      // String enAuthorNames = authorMap.get("enAuthorNames");
      pdwhPublication.setAuthorNameSpec(XmlUtil.formatPubAuthorKws(authorNameSpec));

      Map<String, Object> keywordMap = this.getKeywords(zhKeywords, enKeywords);
      String zhkws = keywordMap.get("zhKeywords") == null ? "" : (String) keywordMap.get("zhKeywords");
      if (StringUtils.isNotBlank(zhkws)) {
        pdwhPublication.setZhKeywords(XmlUtil.formatPubAuthorKws(zhKeywords));
        updatePubKws(job.getHandleId(), 2, zhkws);
      }
      String enkws = keywordMap.get("enKeywords") == null ? "" : (String) keywordMap.get("enKeywords");
      if (StringUtils.isNotBlank(enkws)) {
        pdwhPublication.setEnKeywords(XmlUtil.formatPubAuthorKws(enKeywords));
        updatePubKws(job.getHandleId(), 1, enkws);
      }
      pdwhPublication.setUpdateDate(new Date());
      pdwhPublicationDao.save(pdwhPublication);
      this.updateTaskStatus(job.getJobId(), 1, "更新完毕");
    } catch (DocumentException e) {
      logger.error("读取成果xml更新关键词、标题、作者信息错误，pubId:" + job.getHandleId(), e);
      this.updateTaskStatus(job.getJobId(), 2, "更新关键词、标题、作者信息错误");
    }

  }

  /**
   * 判断中英文作者名是否放错了位置，放错了位置的话调换
   * 
   * @param enAuthorNames
   * @param zhAuthorNames
   * @return
   */
  public Map<String, String> swapAuthorNames(String authorNames, String authorsNamesSpec) {
    Map<String, String> authorMap = new HashMap<String, String>();
    if (StringUtils.isNotEmpty(authorNames) && StringUtils.isNotEmpty(authorsNamesSpec)) {
      // authorNames里面包含中文字符，并且authorsNamesSpec 只有英文字符
      if (XmlUtil.containZhChar(authorNames) && !XmlUtil.containZhChar(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      } else {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
        // 都为中文,以authorsNamesSpec为准
        if (XmlUtil.containZhChar(authorNames) && XmlUtil.containZhChar(authorsNamesSpec)) {
          authorMap.put("zhAuthorNames", authorsNamesSpec);
          authorMap.put("enAuthorNames", "");
        }
        // 都为英文，以authorNames为准
        if (!XmlUtil.containZhChar(authorNames) && !XmlUtil.containZhChar(authorsNamesSpec)) {
          authorMap.put("zhAuthorNames", "");
          authorMap.put("enAuthorNames", authorNames);
        }
      }

    } else {
      if (XmlUtil.containZhChar(authorNames)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      } else if (StringUtils.isNotEmpty(authorNames)) {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
      } else if (XmlUtil.containZhChar(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
      } else if (StringUtils.isNotBlank(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      }
    }
    return authorMap;
  }

  public void updatePubKws(Long pubId, int type, String keywords) {
    pdwhPubKeywordsDao.deletePubKeywords(pubId, type);
    pdwhPubKeywordsDao.save(new PdwhPubKeywords(pubId, keywords, type));
  }

  public Map<String, Object> getKeywords(String zhKeywords, String enKeywords) {
    Map<String, Object> keywordsMap = new HashMap<String, Object>();
    // 英文关键字中包含中文且中文关键字不包含中文，交换，其他情况保留之前的
    if (XmlUtil.containZhChar(enKeywords) && !XmlUtil.containZhChar(zhKeywords)) {
      keywordsMap.put("zhKeywords", enKeywords);
      keywordsMap.put("enKeywords", zhKeywords);
    } else {
      keywordsMap.put("zhKeywords", zhKeywords);
      keywordsMap.put("enKeywords", enKeywords);
    }
    return keywordsMap;
  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String errMsg) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, errMsg);
    } catch (Exception e) {
      logger.error("更新任务执行记录状态出错，jobId:" + jobId, e);
    }

  }

}
