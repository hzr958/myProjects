package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.service.solrindex.AllIndexHandleService;
import com.smate.center.task.service.solrindex.IndexInfoVO;
import com.smate.center.task.single.service.person.UserIndexService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 构建人员索引任务
 * 
 * @author lj
 *
 */
public class UserIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  UserIndexService userIndexService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private AllIndexHandleService allIndexHandleService;

  private Long startId;
  private Long endId;
  private String userKeyValue;

  public UserIndexTask() {
    super();
  }

  public UserIndexTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========UserIndexTask已关闭==========");
      return;
    }
    // 是否移除缓存重构所有人员索引
    if (taskMarkerService.getApplicationQuartzSettingValue("AllIndex_removeUserCache") == 1) {
      cacheService.remove(userKeyValue, "last_psn_id");
    }
    try {
      IndexInfoVO indexInfo = new IndexInfoVO();
      Long lastPsnId = (Long) cacheService.get(userKeyValue, "last_psn_id");
      if (lastPsnId == null) {
        indexInfo.setLastPsnId(startId);
      } else {
        indexInfo.setLastPsnId(lastPsnId);
      }
      indexInfo.setServiceType("userIndex");
      indexInfo.setMaxId(endId);
      allIndexHandleService.runIndex(indexInfo);
      this.cacheService.put(userKeyValue, 60 * 60 * 24, "last_psn_id", indexInfo.getLastPsnId());
    } catch (Exception e) {
      logger.debug("User索引创建出错", e);
    }
  }

  public Long getStartId() {
    return startId;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public Long getEndId() {
    return endId;
  }

  public void setEndId(Long endId) {
    this.endId = endId;
  }

  public String getUserKeyValue() {
    return userKeyValue;
  }

  public void setUserKeyValue(String userKeyValue) {
    this.userKeyValue = userKeyValue;
  }


}
