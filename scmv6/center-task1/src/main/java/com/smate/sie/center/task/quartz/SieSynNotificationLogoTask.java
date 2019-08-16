package com.smate.sie.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SiePubSyncFulltextRefresh;
import com.smate.sie.center.task.service.SieSynSnsPubFulltextService;

/**
 * 同步在SIE中的SNS成果的全文至SIE中的任务
 * 
 * @author 叶星源
 * @Date 201903
 */
public class SieSynNotificationLogoTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private static final long BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SieSynSnsPubFulltextService synSnsPubService;

  public SieSynNotificationLogoTask() {
    super();
  }

  public SieSynNotificationLogoTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    // 同部数数据
    synSnsPubService.syncSnsPubToSie(BATCH_SIZE);
    // 同步动态中对应的SNS的全文信息,获取最大同步数，按同部数计算出需要处理的次数
    syncNewFulltext();
    logger.info("同步动态已完成");
  }

  private void syncNewFulltext() {
    int totle = synSnsPubService.getMaxDynPubNum();
    for (int i = 0; totle / BATCH_SIZE + 1 > i; i++) {
      for (SiePubSyncFulltextRefresh refresh : synSnsPubService.syncSnsPubOnSie(i, BATCH_SIZE)) {
        // 根据同步对象，进行业务处理
        try {
          synSnsPubService.synNewFulltext(refresh);
        } catch (Exception e) {
          logger.error("同步在SIE中的SNS成果的全文至SIE任务异常：", e);
          e.printStackTrace();
        }
      }
    }
  }

}
