package com.smate.sie.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieKpiPayBehaveService;
import com.smate.sie.center.task.service.SieSyncPubSocialBehaveService;
import com.smate.sie.core.base.utils.model.statistics.BhConsts;
import com.smate.sie.core.base.utils.model.statistics.SieSTPubSyncRefresh;

/**
 * 同步成果社交化行为任务
 * 
 * @author hd
 *
 */
public class SieSyncPubSocialBehaveTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  @Autowired
  private SieKpiPayBehaveService sieKpiPayBehaveService;
  @Autowired
  private SieSyncPubSocialBehaveService sieSyncPubSocialBehaveService;


  public SieSyncPubSocialBehaveTask() {
    super();
  }


  public SieSyncPubSocialBehaveTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      return;
    }
    Date nowDate = new Date();
    // 获取表中剩下待处理记录数
    Long count = sieKpiPayBehaveService.cntNeedDeal(nowDate);
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      return;
    } else {
      sieKpiPayBehaveService.setNeedDealPuball(nowDate);
    }
    while (true) {
      // 读取需要统计Id
      List<SieSTPubSyncRefresh> inss = sieKpiPayBehaveService.LoadNeedDealPubRecords(BATCH_SIZE);
      if (inss == null || inss.size() == 0) {
        return;
      }
      for (SieSTPubSyncRefresh refresh : inss) {
        try {
          sieSyncPubSocialBehaveService.doSync(refresh.getInsId());
          refresh.setStatus(BhConsts.STATUS_SUC);
          refresh.setLastRunTime(nowDate);
          sieKpiPayBehaveService.saveSTPubSyncRefresh(refresh);
        } catch (Exception e) {
          refresh.setStatus(BhConsts.STATUS_FAIL);
          refresh.setLastRunTime(nowDate);
          sieKpiPayBehaveService.saveSTPubSyncRefresh(refresh);
          logger.error("同步成果社交化行为任务", e);
        }

      }
    }
  }

}
