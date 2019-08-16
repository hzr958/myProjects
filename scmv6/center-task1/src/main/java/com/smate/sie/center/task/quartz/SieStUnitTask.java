package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieStInsService;
import com.smate.sie.core.base.utils.model.statistics.SieUnitRefresh;

/**
 * 部门统计任务
 * 
 * @author hd
 *
 */
public class SieStUnitTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 100;
  @Autowired
  private SieStInsService<SieUnitRefresh> sieStUnitService;

  public SieStUnitTask() {
    super();
  }

  public SieStUnitTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    sieStUnitService.setNeedCountKeyId();
    // 获取表中剩下待处理记录数
    Long count = sieStUnitService.countNeedCountKeyId();
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      this.sieStUnitService.updateStatus();
    }
    while (true) {
      // 读取需要统计Id
      List<SieUnitRefresh> unitIds = sieStUnitService.loadNeedCountKeyId(batchSize);
      if (unitIds == null || unitIds.size() == 0) {
        return;
      }
      // 有待处理数据
      for (SieUnitRefresh insStatRefresh : unitIds) {
        Long unitId = insStatRefresh.getUnitId();
        try {
          // 统计
          sieStUnitService.doStatistics(unitId);
          // 处理成功
          insStatRefresh.setStatus(1);
          sieStUnitService.saveStRefresh(insStatRefresh);
        } catch (Exception e) {
          // 处理失败
          insStatRefresh.setStatus(99);
          this.sieStUnitService.saveStRefresh(insStatRefresh);
          logger.error("部门统计任务出错", e);

        }
      }
    }


  }

}
