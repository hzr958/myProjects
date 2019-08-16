package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 从项目中提取整理自填关键词与项目关联成果关键词，存入对应表中
 * 
 **/
public class PubToPubsimpleTask3 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数
  private final static String CENTER_BATCH_CACHE_3 = "center_batch_cache_3";

  private Long startPubId = 38611905L;
  private Long endPubId = 106723811L;

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__3===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__3===开关关闭");
      return;
    } else {
      try {
        doRun();

      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__3,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }


  public void doRun() throws BatchTaskException {
    List<BigDecimal> toHandleList = this.pdwhPublicationService.getPrjIdList(SIZE);
    for (BigDecimal pId : toHandleList) {
      Long prjId = pId.longValue();
      Integer rsStatus = 1;
      try {
        rsStatus = this.pdwhPublicationService.extractPrjKws(prjId);
      } catch (Exception e) {
        logger.error("nsfc提取项目关键词出错", e);
        this.pdwhPublicationService.updatePrjStatus(prjId, 3);
      }
      this.pdwhPublicationService.updatePrjStatus(prjId, rsStatus);
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
  }
}
