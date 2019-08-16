package com.smate.center.task.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.service.solrindex.JxProjectIndexService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 读取表jxkjt_prp_info_TEMP,JXSTC_PRP_INFO_TEMP 数据到sorl任务
 * 
 * @author zzl
 *
 */
public class JxProjectIndexTask extends TaskAbstract {
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private JxProjectIndexService jxProjectIndexService;
  @Autowired
  private CacheService cacheService;
  public static String INDEX_PRP_CODE = "prp_code";

  public JxProjectIndexTask() {
    super();
  }

  public JxProjectIndexTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========JxProjectIndexTask已关闭==========");
      return;
    }
    // 是否移除缓存重构所有人员索引
    if (taskMarkerService.getApplicationQuartzSettingValue("JxProjectIndexTask_removeUserCache") == 1) {
      cacheService.remove(INDEX_PRP_CODE, "last_jxstc_prp_code");
      cacheService.remove(INDEX_PRP_CODE, "last_jxkjt_prp_code");

    }

    try {
      logger.info("=========JxProjectIndexTask开始运行==========");
      jxProjectIndexService.indexJxProject();

    } catch (Exception e) {
      logger.error("JxProjectIndexTask,运行异常", e);
    }

  }
}
