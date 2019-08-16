package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubToPubsimpleTask1 {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;


  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__1===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__1===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__1,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    this.taskMarkerService.closeQuartzApplication("PubToPubsimpleTask1");
    while (true) {
      List<Map<String, Object>> pubIdList = this.nsfcKeywordsService.getEncodedPubIdList(5000, 0L);
      if (pubIdList == null || pubIdList.size() == 0) {
        break;
      }
      for (Map<String, Object> mp : pubIdList) {
        Long id = ((BigDecimal) mp.get("PUB_SEQ_ID")).longValue();
        if (mp.get("TITLE") == null) {
          this.nsfcKeywordsService.updateNsfcPrjPubStatus(id, 8);
          continue;
        }
        String title = (String) mp.get("TITLE");
        String category = (String) mp.get("CATEGORY");
        try {
          Long pdwhPubId = this.nsfcKeywordsService.getPdwhPubId(title);
          if (pdwhPubId == null || pdwhPubId == 0L) {
            this.nsfcKeywordsService.updateNsfcPrjPubStatus(id, 9);
            continue;
          }
          List<String> kws = this.nsfcKeywordsService.getPdwhPubKwsFromDb(pdwhPubId);
          if (kws == null || kws.size() <= 0) {
            this.nsfcKeywordsService.updateNsfcPrjPubStatus(id, 2);
            continue;
          }
          // 如果存在就不再插入关键词了
          this.nsfcKeywordsService.saveKws(kws, pdwhPubId, category);
          this.nsfcKeywordsService.updateNsfcPrjPubStatus(id, 1);
        } catch (Exception e) {
          this.nsfcKeywordsService.updateNsfcPrjPubStatus(id, 3);
          logger.error("pubToPubsimpleTask__1 成果关键词出错, pubTitle=" + title, e);
        }
      }
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask1") == 1;
  }
}
