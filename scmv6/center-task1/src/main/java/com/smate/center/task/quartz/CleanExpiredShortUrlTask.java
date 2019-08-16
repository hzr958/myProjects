package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.CleanExpiredShortUrlService;

/**
 * 清理过期短地址任务
 * 
 * @author LJ
 *
 */
public class CleanExpiredShortUrlTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CleanExpiredShortUrlService cleanExpiredShortUrlService;

  public CleanExpiredShortUrlTask() {
    super();
  }

  public CleanExpiredShortUrlTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    List<Long> needCleanData = null;
    try {
      needCleanData = cleanExpiredShortUrlService.getNeedCleanData();
      if (CollectionUtils.isEmpty(needCleanData)) {
        logger.info("短地址清理完毕！");
        return;
      }
    } catch (Exception e) {
      logger.error("获取需要清理的短地址出错！", e);
    }

    for (Long Id : needCleanData) {
      try {
        cleanExpiredShortUrlService.handleUrldata(Id);
      } catch (Exception e) {
        logger.error("清理的短地址出错！Id:" + Id, e);
      }
    }


  }
}
