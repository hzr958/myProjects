package com.smate.center.batch.quartz;

import java.util.List;
import java.util.Map;

import org.ansj.library.DicLibrary;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.google.gdata.util.common.base.StringUtil;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 按照词典提取中英文关键词，并通过计算其长度1-5的关键词子集与知识库对照，最终排序得到前三子集的已有分类，作为成果或者项目的分类。 排序按照子集长度优先；如果同样长度，co-tf优先
 * 
 * 
 */
public class PubToPubsimpleTask7 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 500; // 每次刷新获取的个数

  private Long startPubId = 19336069L;
  private Long endPubId = 19904911043L;

  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__7===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__7===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__7,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // 只加载一次
    if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache7") == 1) {
      List<String> kwStrsZh = this.projectDataFiveYearService.getNsfcKwByLanguage(1);
      if (!CollectionUtils.isEmpty(kwStrsZh)) {
        // DicLibrary.clear(DicLibrary.DEFAULT);
        // 加载自定义词典
        for (String kw : kwStrsZh) {
          if (StringUtils.isEmpty(kw) || StringUtils.isEmpty(kw.trim())) {
            continue;
          }
          DicLibrary.insert(DicLibrary.DEFAULT, kw.toLowerCase().trim(), "nsfc_kws", DicLibrary.DEFAULT_FREQ);
        }
      }
      List<String> kwStrsEn = this.projectDataFiveYearService.getNsfcKwByLanguage(2);
      if (!CollectionUtils.isEmpty(kwStrsEn)) {
        // DicLibrary.clear(DicLibrary.DEFAULT);
        // 加载自定义词典
        for (String kw : kwStrsEn) {
          if (StringUtils.isEmpty(kw) || StringUtils.isEmpty(kw.toLowerCase())) {
            continue;
          }
          kw = kw.toLowerCase().trim().replaceAll("\\s+", "空格");
          DicLibrary.insert(DicLibrary.DEFAULT, kw, "nsfc_kws", DicLibrary.DEFAULT_FREQ);
        }
      }
      taskMarkerService.closeQuartzApplication("PubToPubsimpleTask_removePubCache7");
    }
    Long count = 0L;
    while (true) {
      count++;
      List<Object> dataList = projectDataFiveYearService.findPatentList(SIZE, startPubId, endPubId);
      if (dataList == null || dataList.size() == 0 || count.equals(3999999L)) {
        logger.info("PubToPubsimpleTask7已经完成");
        break;
      }
      for (Object obj : dataList) {
        Long pubId = Long.parseLong(obj.toString());
        Integer resStatus = 1;
        String resMsg = "执行成功";
        try {
          String xml = projectDataFiveYearService.findPatenXml(pubId);
          PubXmlDocument doc = new PubXmlDocument(xml);
          Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

          String ctitle = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
          String cabstract = StringUtils.trimToEmpty(ele.attributeValue("cabstract"));
          String ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
          if (StringUtils.isBlank(ctitle)) {
            ctitle = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
          }
          if (StringUtils.isBlank(cabstract)) {
            cabstract = StringUtils.trimToEmpty(ele.attributeValue("eabstract"));
          }
          if (StringUtils.isBlank(ckeywords)) {
            ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ekeywords"));
          }
          String approveCode = pubId.toString();
          String content = ctitle + cabstract + ckeywords;
          Map<Integer, Map<Integer, String>> zhContentMap = null;
          if (!StringUtil.isEmpty(content)) {
            // 全角转半角
            String zhContent = XmlUtil.changeSBCChar(content);
            zhContentMap = projectDataFiveYearService.getKwSubsetsFromPubContent(zhContent);
            this.projectDataFiveYearService.classifyByKeywords(zhContentMap, approveCode, 0);
          }
        } catch (Exception e) {
          logger.error("PubToPubsimpleTask7 -- 关键词组合出错  pubid ：" + pubId, e);
          resStatus = 2;
          resMsg = "--PubToPubsimpleTask7--pubId=" + pubId + "," + e.getMessage();
          if (resMsg.length() > 1000) {
            resMsg = resMsg.substring(0, 1000);
          }
        }
        projectDataFiveYearService.updatePatentLog(pubId, resStatus, resMsg);
      }

    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("PatentClassfiTask7") == 1;
  }
}
