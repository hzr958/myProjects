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
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubToPubsimpleTask17 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 500; // 每次刷新获取的个数

  private Long startPubId = 19906470897L;
  private Long endPubId = 19909096248L;

  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__17===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__17===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__17,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache17") == 1) {

      taskMarkerService.closeQuartzApplication("PubToPubsimpleTask_removePubCache17");
    }
    Long count = 0L;
    while (true) {
      count++;
      List<Object> dataList = projectDataFiveYearService.findPatentList(SIZE, startPubId, endPubId);
      if (dataList == null || dataList.size() == 0 || count.equals(3999999L)) {
        logger.info("PubToPubsimpleTask17已经完成");
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
          resStatus = 2;
          logger.error("PubToPubsimpleTask17 -- 关键词组合出错  pubid ：" + pubId, e);
          resMsg = "--PubToPubsimpleTask17--pubId=" + pubId + "," + e.getMessage();
          if (resMsg.length() > 1500) {
            resMsg = resMsg.substring(0, 1500);
          }
        }
        projectDataFiveYearService.updatePatentLog(pubId, resStatus, resMsg);
      }
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("PatentClassfiTask17") == 1;
  }
}
