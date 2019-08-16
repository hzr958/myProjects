package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.PatentIndexService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 重构专利索引
 * 
 * @author lj
 *
 */
@Deprecated
public class PatentIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public PatentIndexTask() {
    super();
  }

  public PatentIndexTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private CacheService cacheService;
  @Autowired
  PatentIndexService patentIndexService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  public static String INDEX_TYPE_PAT = "patent_index";

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    // 是否移除缓存重构所有专利索引
    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_removePatCache") == 1) {
      cacheService.remove(INDEX_TYPE_PAT, "last_patent_id");
    }
    try {
      patentIndexService.indexPatent();

    } catch (Exception e) {
      logger.error("PatentIndexTask,运行异常", e);
    }

  }

}
