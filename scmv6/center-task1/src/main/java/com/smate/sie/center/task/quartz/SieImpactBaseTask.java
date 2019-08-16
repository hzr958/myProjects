package com.smate.sie.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieImpactBaseService;
import com.smate.sie.center.task.service.SieKpiPayImpactService;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.SieSTImpactSyncRefresh;

/**
 * 单位影响力基表任务
 * 
 * @author hd
 *
 */
public class SieImpactBaseTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  @Autowired
  private SieKpiPayImpactService<SieSTImpactSyncRefresh> sieKpiPayImpactService;
  @Autowired
  private SieImpactBaseService sieImpactBaseService;

  public SieImpactBaseTask() {
    super();
  }


  public SieImpactBaseTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      return;
    }
    Date nowDate = new Date();
    // 获取表中剩下待处理记录数
    Long count = sieKpiPayImpactService.cntNeedDeal(nowDate);
    // 没有待处理记录数则返回
    if (count.intValue() == 0) {
      return;
    } else {
      // 有待处理记录数，则根据KPI_PAY_IMPACT 待处理记录，更新数据到 ST_IMPACT_BASE_REFRESH
      sieKpiPayImpactService.setNeedDealall(nowDate);
    }
    while (true) {
      // 读取需要统计Id
      List<SieSTImpactSyncRefresh> inss = sieKpiPayImpactService.LoadNeedDealRecords(BATCH_SIZE);
      if (inss == null || inss.size() == 0) {
        return;
      }
      for (SieSTImpactSyncRefresh refresh : inss) {
        // 每次处理一个单位
        try {

          Date startDate = new Date();
          if (!sieImpactBaseService.checkRepeat(refresh.getInsId())) {
            sieImpactBaseService.doDeal(refresh.getInsId());
          }
          Date endDate = new Date();
          Long useTime = (endDate.getTime() - startDate.getTime());

          refresh.setStatus(ImpactConsts.STATUS_SUC);
          refresh.setLastRunTime(nowDate);
          refresh.setLastStartTime(startDate);
          refresh.setLastEndTime(endDate);
          refresh.setLastUseTime(useTime);
          sieKpiPayImpactService.saveSieSTImpactSyncRefresh(refresh);
        } catch (Exception e) {
          refresh.setStatus(ImpactConsts.STATUS_FAIL);
          refresh.setLastRunTime(nowDate);
          sieKpiPayImpactService.saveSieSTImpactSyncRefresh(refresh);
          logger.error("SieImpactBaseTask任务，社交化行为处理异常", e);
        }

      }
    }
  }


}
