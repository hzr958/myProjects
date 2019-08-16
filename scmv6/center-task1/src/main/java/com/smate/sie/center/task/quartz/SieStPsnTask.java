package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieStInsService;
import com.smate.sie.core.base.utils.model.statistics.SiePsnRefresh;

/**
 * 人员统计任务
 * 
 * @author hd
 *
 */
public class SieStPsnTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 100;
  @Autowired
  private SieStInsService<SiePsnRefresh> sieStPsnService;

  public SieStPsnTask() {
    super();
  }

  public SieStPsnTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    sieStPsnService.setNeedCountKeyId();
    // 获取表中剩下待处理记录数
    Long count = sieStPsnService.countNeedCountKeyId();
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      return;
    }
    while (true) {
      // 读取需要统计的insId
      List<SiePsnRefresh> psnIds = sieStPsnService.loadNeedCountKeyId(batchSize);
      if (psnIds == null || psnIds.size() == 0) {
        return;
      }
      // 有待处理数据
      for (SiePsnRefresh insStatRefresh : psnIds) {
        Long psnId = insStatRefresh.getPsnId();
        try {
          // 统计
          sieStPsnService.doStatistics(psnId);
          // 处理成功
          insStatRefresh.setStatus(1);
          sieStPsnService.saveStRefresh(insStatRefresh);
        } catch (Exception e) {
          // 处理失败
          insStatRefresh.setStatus(99);
          this.sieStPsnService.saveStRefresh(insStatRefresh);
          logger.error("人员统计任务出错", e);

        }
      }
    }


  }

}
