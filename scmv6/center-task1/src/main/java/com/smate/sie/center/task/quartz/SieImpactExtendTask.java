package com.smate.sie.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieImpactExtendService;
import com.smate.sie.center.task.service.SieKpiPayImpactService;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.SieSTImpactExtendRefresh;

/**
 * 单位影响力拓展表表任务
 * 
 * @author hd
 *
 */
public class SieImpactExtendTask extends TaskAbstract {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieKpiPayImpactService<SieSTImpactExtendRefresh> sieKpiPayImpactExtendService;
  @Autowired
  private SieImpactExtendService sieImpactExtendService;

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  public SieImpactExtendTask() {
    super();
  }


  public SieImpactExtendTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    Date nowDate = new Date();
    // 获取表中剩下待处理记录数
    Long count = sieKpiPayImpactExtendService.cntNeedDeal(nowDate);
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      return;
    } else {
      sieKpiPayImpactExtendService.setNeedDealall(nowDate);
    }
    while (true) {
      // 读取需要统计Id
      List<SieSTImpactExtendRefresh> inss = sieKpiPayImpactExtendService.LoadNeedDealRecords(BATCH_SIZE);
      if (inss == null || inss.size() == 0) {
        return;
      }
      for (SieSTImpactExtendRefresh refresh : inss) {
        try {
          /* 基表任务已完成，再处理当前数据 */
          if (sieKpiPayImpactExtendService.alreadyDealBase(refresh.getInsId())) {
            Date startDate = new Date();
            if (!sieImpactExtendService.checkRepeat(refresh.getInsId())) {
              sieImpactExtendService.doCalculate(refresh.getInsId(), nowDate);
            }
            Date endDate = new Date();
            Long useTime = (endDate.getTime() - startDate.getTime());


            refresh.setStatus(ImpactConsts.STATUS_SUC);
            refresh.setLastRunTime(nowDate);
            refresh.setLastStartTime(startDate);
            refresh.setLastEndTime(endDate);
            refresh.setLastUseTime(useTime);
            sieKpiPayImpactExtendService.saveSieSTImpactSyncRefresh(refresh);
          }
        } catch (Exception e) {
          refresh.setStatus(ImpactConsts.STATUS_FAIL);
          refresh.setLastRunTime(nowDate);
          sieKpiPayImpactExtendService.saveSieSTImpactSyncRefresh(refresh);
          logger.error("SieImpactExtendTask任务，社交化行为处理异常", e);
        }

      }
    }

  }



}
