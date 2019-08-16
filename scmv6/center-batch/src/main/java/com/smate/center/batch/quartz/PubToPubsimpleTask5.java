package com.smate.center.batch.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.ansj.library.DicLibrary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.util.common.base.StringUtil;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 部分基准库成果，对应标准期刊有问题，修正。 期刊名称没有改变，但是issn变了
 * 
 */
public class PubToPubsimpleTask5 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;

  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__6===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__6===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__6,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    while (true) {
      List<BigDecimal> pubList = this.projectDataFiveYearService.getTohandlePdwhPubJournal();
      if (pubList == null || pubList.size() == 0) {
        logger.info("PubToPubsimpleTask6已经完成");
        break;
      }

      for (BigDecimal id : pubList) {
        try {
          Long pubId = id.longValue();
          String xml = this.projectDataFiveYearService.getXmlStr(pubId);
          if (!StringUtil.isEmpty(xml)) {
            ImportPubXmlDocument doc = new ImportPubXmlDocument(xml);
            String journalName = doc.getOriginal();
            String issn = doc.getIssn();
            if (StringUtils.isEmpty(journalName) && StringUtils.isEmpty(issn)) {
              projectDataFiveYearService.updateTohandlePdwhPubJournal(7, pubId);
              continue;
            }
            // 全角转半角
            Long jnlId = pdwhPublicationService.getJnlIdByJournalNameOrIssn(journalName, issn);
            if (jnlId == null || jnlId == 0L) {
              projectDataFiveYearService.updateTohandlePdwhPubJournal(2, pubId);
              continue;
            }
            this.projectDataFiveYearService.updateTohandlePdwhPubJournalJnlId(jnlId, pubId);
          }
        } catch (Exception e) {
          logger.error("PubToPubsimpleTask6 -- 关键词组合出错  pubid ：" + id, e);
        }
      }
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return
    // taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask6")
    // == 1;
  }
}
