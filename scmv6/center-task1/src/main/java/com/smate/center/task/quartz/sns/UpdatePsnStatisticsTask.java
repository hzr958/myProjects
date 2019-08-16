package com.smate.center.task.quartz.sns;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.single.service.person.PsnStatisticsService;
import com.smate.core.base.psn.model.PsnStatisticsRefresh;

/**
 * 更新个人统计数.
 * 
 * @author tsz
 *
 * @date 2018年9月18日
 */
public class UpdatePsnStatisticsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final Integer SIZE = 200; // 每次处理的个数
  @Autowired
  private PsnStatisticsService psnStatisticsService;

  public UpdatePsnStatisticsTask() {
    super();
  }

  public UpdatePsnStatisticsTask(String beanName) {
    super(beanName);
  }

  /**
   * 任务启动.
   * 
   * @throws Exception
   * 
   */
  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      logger.info("=========UpdatePsnStatisticsTask已关闭==========");
      return;
    }
    try {
      psnStatisticsService.ToRefresh();
    } catch (Exception e1) {
      logger.error("同步数据到刷新表出错!", e1);
      return;
    }
    Long startId = 0L;
    while (true) {
      List<PsnStatisticsRefresh> psnList;
      try {
        psnList = psnStatisticsService.getToBeRefresh(startId, SIZE);
      } catch (Exception e1) {
        logger.error("获取待更新的数据出错", e1);
        return;
      }
      if (CollectionUtils.isEmpty(psnList)) {
        return;
      }
      for (PsnStatisticsRefresh psr : psnList) {
        // startId = psr.getPsnId(); //暂时不按id来查
        try {
          psnStatisticsService.updatePsnStatistics(psr.getPsnId());
          psr.setStatus(1); // 暂时不按id来查
        } catch (Exception e) {
          psr.setStatus(2);
          logger.error("更新个人统计数出错.psnId=" + psr.getPsnId(), e);
          psr.setMsg(e.getMessage());
        }
        psr.setUpdateDate(new Date());
        psnStatisticsService.updatePsnStatisticsRefresh(psr);
      }
    }
  }
}
