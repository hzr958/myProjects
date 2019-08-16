package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 计算pdwh成果补充关键词（来源：自填关键词与项目关联成果关键词）的tf与长度为2的子集co-tf，的详细信息，用于最终关键词排序与推荐
 * 
 */
public class KGPubKwsExtractTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;

  public void run() throws BatchTaskException {
    logger.debug("====================================KGPubKwsExtractTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================KGPubKwsExtractTask===开关关闭");
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
    List<BigDecimal> pdwhPubIdList = nsfcKeywordsService.getPdwhPubToHandleKwList(SIZE);
    if (pdwhPubIdList == null || pdwhPubIdList.size() == 0) {
      logger.info("====================================KGPubKwsExtractTask===运行完毕");
      return;
    }
    for (BigDecimal id : pdwhPubIdList) {
      Long prjId = id.longValue();
      Integer rs = 0;
      try {
        rs = this.nsfcKeywordsService.extractPdwhPubKws(prjId);
        this.nsfcKeywordsService.updatePdwhPubStatus(prjId, rs);
      } catch (Exception e) {
        logger.error("nsfc项目计算项目cotf错误,prjId:" + prjId, e);
        this.nsfcKeywordsService.updatePdwhPubStatus(prjId, rs);
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
