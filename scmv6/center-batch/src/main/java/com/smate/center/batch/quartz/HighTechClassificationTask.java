package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.HighTechKeywordsService;
import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 按提取关键词对成果进行 高新产业分类
 * 
 */
public class HighTechClassificationTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private HighTechKeywordsService highTechKeywordsService;
  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;


  public void run() throws BatchTaskException {
    logger.debug("====================================HighTechClassificationTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================HighTechClassificationTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("KGPubKwsExtractTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    BigDecimal num = this.highTechKeywordsService.getHighTechPubToHandleNum();
    if (num != null && num.longValue() > 0L) {
      this.generateAddrPsnConstDicService.generateHighTechKwsDic();
    } else {
      logger.debug("====================================HighTechClassificationTask===计算完成");
      return;
    }
    while (true) {
      List<BigDecimal> pubList = this.highTechKeywordsService.getHighTechPubToHandleList(SIZE);
      if (pubList == null || pubList.size() == 0) {
        logger.info("====================================HighTechClassificationTask===运行完毕");
        break;
      }
      for (BigDecimal id : pubList) {
        Long pubId = id.longValue();
        Integer rs = 0;
        try {
          rs = this.highTechKeywordsService.HighTechClassificationForPub(pubId);
          this.highTechKeywordsService.updateHighTechPubStatus(pubId, rs);
        } catch (Exception e) {
          logger.error("HighTechClassificationTask错误,pubId:" + pubId, e);
          highTechKeywordsService.updateHighTechPubStatus(pubId, 3);
        }
      }
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return
    // taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2")
    // == 1;
  }

}
