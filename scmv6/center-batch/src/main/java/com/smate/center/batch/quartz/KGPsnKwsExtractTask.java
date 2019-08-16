package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 计算pdwh成果补充关键词（来源：自填关键词与项目关联成果关键词）的tf与长度为2的子集co-tf，的详细信息，用于最终关键词排序与推荐
 * 
 */
public class KGPsnKwsExtractTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;
  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;


  public void run() throws BatchTaskException {
    logger.debug("====================================KGPsnKwsExtractTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================KGPsnKwsExtractTask===开关关闭");
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
    // SIZE
    // DicLibrary.clear(DicLibrary.DEFAULT);
    // this.generateAddrPsnConstDicService.generateNsfcKwsDic();
    while (true) {
      List<BigDecimal> psnList = nsfcKeywordsService.getPsnToHandleKwList(SIZE);
      if (psnList == null || psnList.size() == 0) {
        logger.info("====================================KGPsnKwsExtractTask===运行完毕");
        break;
      }
      for (BigDecimal id : psnList) {
        Long psnId = id.longValue();
        Integer rs = 0;
        try {
          rs = this.nsfcKeywordsService.extractPsnKwsFromPrjAndPubExcludeTf(psnId);
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
    // return
    // taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2")
    // == 1;
  }

}
