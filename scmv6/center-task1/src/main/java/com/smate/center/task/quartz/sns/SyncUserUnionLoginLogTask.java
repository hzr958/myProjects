package com.smate.center.task.quartz.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.SyncUserUnionLoginLogService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;

public class SyncUserUnionLoginLogTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private SyncUserUnionLoginLogService syncUserUnionLoginLogService;
  public static String PSN_ID_CACHE = "user_union_login_cache";

  public SyncUserUnionLoginLogTask() {
    super();
  }

  public SyncUserUnionLoginLogTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SyncUserUnionLoginLogTask已关闭==========");
      return;
    }

    // 是否移除psn_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("PsnFundRecommendTask_removePatCache") == 1) {
      cacheService.remove(PSN_ID_CACHE, "last_psn_id");
    }
    Long lastPsnId = (Long) cacheService.get(PSN_ID_CACHE, "last_psn_id");
    if (lastPsnId == null) {
      lastPsnId = 0L;
    }
    List<Long> psnIdList = syncUserUnionLoginLogService.getLoginPsnIds(lastPsnId);
    // 处理完登录过的人员，处理未登录过但是关联过其他业务系统
    if (CollectionUtils.isEmpty(psnIdList)) {
      psnIdList = syncUserUnionLoginLogService.getUnionPsnId();
      if (CollectionUtils.isEmpty(psnIdList)) {
        cacheService.remove(PSN_ID_CACHE, "last_psn_id");
      } else {
        // 保存记录至user_union_login_log表
        syncUserUnionLoginLogService.saveUserUnionLoginLog(psnIdList, false);
      }
    } else {
      syncUserUnionLoginLogService.saveUserUnionLoginLog(psnIdList, true);
      this.cacheService.put(PSN_ID_CACHE, 60 * 60 * 24, "last_psn_id", psnIdList.get(psnIdList.size() - 1));
    }



  }
}
