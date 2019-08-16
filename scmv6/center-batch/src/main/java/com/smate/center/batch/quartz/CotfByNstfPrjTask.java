package com.smate.center.batch.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 根据近10年项目计算关键词集合长度2-5的COTF
 * 
 */
public class CotfByNstfPrjTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;
  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;


  public void run() throws BatchTaskException {
    logger.debug("====================================CotfByNstfPrjTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================CotfByNstfPrjTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("CotfByNstfPrjTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // SIZE
    List<String> pdwhPubIdList = nsfcKeywordsService.getNsfcCategoryToHandleKwList(0, SIZE);
    if (pdwhPubIdList == null || pdwhPubIdList.size() == 0) {
      logger.info("====================================CotfByNstfPrjTask===运行完毕");
      return;
    }
    for (String category : pdwhPubIdList) {
      Integer rs = 0;
      try {
        // 先算中文
        this.generateAddrPsnConstDicService.generateNsfcKwsDicByCategory(category);
        rs = this.nsfcKeywordsService.getKwSubsetsCotf(category, 1);
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, rs);
      } catch (Exception e) {
        logger.error("nsfc项目计算项目cotf错误,category:" + category, e);
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, rs);
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
