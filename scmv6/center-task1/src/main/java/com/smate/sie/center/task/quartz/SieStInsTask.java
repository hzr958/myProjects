package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieStInsService;
import com.smate.sie.core.base.utils.model.statistics.SieInsRefresh;

/**
 * 单位统计任务
 * 
 * @author hd
 *
 */
public class SieStInsTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 100;
  @Autowired
  private SieStInsService<SieInsRefresh> sieStInsService;

  public SieStInsTask() {
    super();
  }

  public SieStInsTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    sieStInsService.setNeedCountKeyId();
    // 获取表中剩下待处理记录数
    Long count = sieStInsService.countNeedCountKeyId();
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      this.sieStInsService.updateStatus();
    }
    while (true) {
      // 读取需要统计的insId
      List<SieInsRefresh> insIds = sieStInsService.loadNeedCountKeyId(batchSize);
      if (insIds == null || insIds.size() == 0) {
        return;
      }
      // 有待处理数据
      for (SieInsRefresh insStatRefresh : insIds) {
        Long insId = insStatRefresh.getInsId();
        try {
          // 统计
          sieStInsService.doStatistics(insId);
          // 处理成功
          insStatRefresh.setStatus(1);
          sieStInsService.saveStRefresh(insStatRefresh);
        } catch (Exception e) {
          // 处理失败
          insStatRefresh.setStatus(99);
          this.sieStInsService.saveStRefresh(insStatRefresh);
          logger.error("单位统计任务出错", e);

        }
      }
    }


  }

}
