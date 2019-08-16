package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.person.UserIndexService;
import com.smate.center.task.single.service.pub.PatentIndexService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 专利推荐用专用索引
 * 
 *
 */

public class PatentRcmdIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public PatentRcmdIndexTask() {
    super();
  }

  public PatentRcmdIndexTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private PatentIndexService patentIndexService;

  @Autowired
  private UserIndexService userIndexService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;

  public static String INDEX_TYPE_PAT_RCMD = "patent_rcmd_index";
  public static String INDEX_TYPE_PAT_OWNER_RCMD = "patent_rcmd_owner_index";
  public static String INDEX_TYPE_PAT_REQUEST_RCMD = "patent_rcmd_request_index";

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    try {
      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_removePatRcmdCache") == 1) {
        cacheService.remove(INDEX_TYPE_PAT_RCMD, "last_patent_id");
      }

      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_removePatReqRcmdCache") == 1) {
        cacheService.remove(INDEX_TYPE_PAT_REQUEST_RCMD, "last_req_id");
      }

      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_removePatPsnRcmdCache") == 1) {
        cacheService.remove(INDEX_TYPE_PAT_OWNER_RCMD, "last_psn_id");
      }

      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_PatRcmd") == 1) {
        patentIndexService.indexPatentRcmd();
      }

      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_PatReqRcmd") == 1) {
        patentIndexService.indexPatentRequestRcmd();
      }

      if (taskMarkerService.getApplicationQuartzSettingValue("PatentRcmdIndexTask_PatPsnRcmd") == 1) {
        userIndexService.indexPatentOwner();
      }
    } catch (Exception e) {
      logger.error("PatentIndexTask,运行异常", e);
    }

  }

}
