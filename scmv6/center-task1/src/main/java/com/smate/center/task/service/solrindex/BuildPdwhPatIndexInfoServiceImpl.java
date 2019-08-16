package com.smate.center.task.service.solrindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.utils.MessageDigestUtils;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Transactional(rollbackFor = Exception.class)
public class BuildPdwhPatIndexInfoServiceImpl extends AbstractIndexHandleServiceImpl {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Override
  public Map<String, Object> checkData(IndexInfoVO indexInfoVO) {
    Map<String, Object> map = new HashMap<>();
    if (indexInfoVO.getLastPatId() == null) {
      map.put("result", "error");
      map.put("msg", "查询的pubId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryBaseData(IndexInfoVO indexInfoVO) {
    indexInfoVO.setPdwhPubList(pubPdwhDAO.findPatByBatchSize(indexInfoVO.getLastPatId(), indexInfoVO.getMaxPubId()));
  }

  @Override
  public void buildIndexData(IndexInfoVO indexInfoVO) {
    List<PubPdwhPO> pdwhPubList = indexInfoVO.getPdwhPubList();
    if (CollectionUtils.isNotEmpty(pdwhPubList)) {
      ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
      for (PubPdwhPO pdwhPub : pdwhPubList) {
        try {
          PubPdwhDetailDOM detail = pubPdwhDetailDAO.findById(pdwhPub.getPubId());
          if (detail != null) {
            PatentInfoBean infoBean = (PatentInfoBean) detail.getTypeInfo();
            SolrInputDocument doc = new SolrInputDocument();
            // 必须字段设定schema.xml配置
            doc.setField("businessType", IndexInfoVO.INDEX_TYPE_PDWH_PAT);
            Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
            doc.setField("env", runEnv);
            doc.setField("id", generateIdForIndex(detail.getPubId(), IndexInfoVO.INDEX_TYPE_PDWH_PAT));
            doc.setField("patId", detail.getPubId());
            doc.setField("patDbId", detail.getSrcDbId());
            doc.setField("patTitle", detail.getTitle());
            doc.setField("patAuthors", detail.getAuthorNames());
            doc.setField("patCitations", detail.getCitations());
            try {
              String cleanAuthors = XmlUtil.cleanXMLAuthorChars(detail.getAuthorNames());
              if (cleanAuthors != null) {
                String[] authors = cleanAuthors.split(";");
                StringBuffer md5Authors = new StringBuffer();
                for (int i = 0; i < authors.length - 1; i++) {
                  md5Authors.append(MessageDigestUtils.messageDigest(authors[i]).trim());
                  md5Authors.append("; ");
                }
                md5Authors.append(MessageDigestUtils.messageDigest(authors[authors.length - 1].trim()));
                doc.setField("cleanPatAuthors", cleanAuthors);
                doc.setField("md5PatAuthors", md5Authors);
              }
            } catch (Exception e) {
              logger.error("生成cleanAuthors出错", e);
            }
            if (pdwhPub.getPublishYear() != null) {
              doc.setField("patYear", pdwhPub.getPublishYear());
            }
            if (pdwhPub.getPublishMonth() != null) {
              doc.setField("patMonth", pdwhPub.getPublishMonth());
            }
            if (pdwhPub.getPublishDay() != null) {
              doc.setField("patDay", pdwhPub.getPublishDay());
            }
            if (StringUtils.isNotBlank(infoBean.getType()) && isNumeric(infoBean.getType())) {
              doc.setField("patTypeId", Long.valueOf(infoBean.getType()));
            } else {
              doc.setField("patTypeId", 7);
            }
            doc.setField("organization", detail.getOrganization());
            doc.setField("patentNo", infoBean.getApplicationNo());
            doc.setField("patBrief", detail.getBriefDesc());
            buildPdwhPubRegionIds(pdwhPub.getPubId(), doc);// 构造成果地区
            buildPdwhPubShortUrl(detail.getPubId(), doc);// 构造成果短地址
            buildPdwhPubCategory(pdwhPub.getPubId(), doc);// 构造成果研究领域
            buildPdwhPubFulltext(pdwhPub.getPubId(), doc);// 构造全文信息
            docList.add(doc);
          }
        } catch (Exception e) {
          logger.error("基准库专利数据错误，pubid:", pdwhPub.getPubId(), e);
        }
      }
      if (saveIndex(docList)) {
        indexInfoVO.setStatus("success");
        indexInfoVO.setMsg("创建专利索引成功");
      } else {
        indexInfoVO.setStatus("error");
        indexInfoVO.setMsg("创建专利索引失败");
      }
      indexInfoVO.setLastPatId(pdwhPubList.get(pdwhPubList.size() - 1).getPubId());
    }
  }

  /**
   * 判断是否为数字.
   * 
   * @param str
   * @return
   */
  public boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

}
