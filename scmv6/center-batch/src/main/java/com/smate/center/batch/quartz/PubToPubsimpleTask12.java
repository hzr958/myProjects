package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubToPubsimpleTask12 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  private Long startGroupId = 100000001190179L;

  private Long endGroupId = 100000001248860L;

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;


  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__12===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__12===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__12,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    while (true) {
      List<BigDecimal> psnList = nsfcKeywordsService.getPsnToHandleKwList(SIZE, 12);
      if (psnList == null || psnList.size() == 0) {
        logger.info("====================================KGPsnKwsExtractTask===运行完毕");
        break;
      }
      for (BigDecimal id : psnList) {
        Long psnId = id.longValue();
        Integer rs = 0;
        try {
          rs = this.nsfcKeywordsService.extractPsnKwsFromPrjAndPub(psnId);
          this.nsfcKeywordsService.updatePsnStatus(psnId, rs);
        } catch (Exception e) {
          logger.error("计算个人关键词错误,psnId:" + psnId, e);
          this.nsfcKeywordsService.updatePsnStatus(psnId, rs);
        }
      }
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask12") == 1;
  }
}
