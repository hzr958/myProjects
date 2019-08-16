package com.smate.center.task.quartz.viststatis;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.psn.VistStatistics;
import com.smate.center.task.service.vistStatistics.UpdateVistStatisService;
import com.smate.core.base.utils.cache.CacheService;

public class updateVistStatisticsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  private UpdateVistStatisService updateVistStatisService;
  @Autowired
  private CacheService cacheService;

  public updateVistStatisticsTask() {
    super();
  }

  public updateVistStatisticsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========updateVistStatisticsTask已关闭==========");
      return;

    }
    Long starId = (Long) cacheService.get("updateVistStatisticsTask", "last_id");
    starId = starId == null ? 0L : starId;
    List<VistStatistics> vistList;
    try {
      vistList = updateVistStatisService.getVistStatisId(starId, SIZE);
      if (CollectionUtils.isNotEmpty(vistList)) {
        Long lastId = vistList.get(vistList.size() - 1).getId();
        this.cacheService.put("updateVistStatisticsTask", 60 * 60 * 24, "last_id", lastId);
        for (VistStatistics vist : vistList) {
          updateVistStatisService.updateVistRegionId(vist);
        }
      } else {
        try {
          this.cacheService.remove("updateVistStatisticsTask", "last_id");
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭updateVistStatisticsTask出错！", e);
        }
      }
    } catch (Exception e) {
      logger.error("更新vist_statistics表省份字段出错，starId=" + starId, e);
    }

  }
}
