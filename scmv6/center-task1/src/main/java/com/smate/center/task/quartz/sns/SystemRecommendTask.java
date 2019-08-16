package com.smate.center.task.quartz.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.RecommendService;
import com.smate.center.task.service.sns.psn.SystemRecommendLoginService;
import com.smate.center.task.service.sns.psn.SystemRecommendService;
import com.smate.center.task.single.service.person.PersonManager;

/**
 * 好友智能推荐.
 * 
 * @author zll
 * 
 */
public class SystemRecommendTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private int batchSize = 5;
  @Autowired
  private RecommendService recommendService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SystemRecommendService systemRecommendService;
  @Autowired
  private SystemRecommendLoginService systemRecommendLoginService;

  public SystemRecommendTask() {
    super();
  }

  public SystemRecommendTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IndexInfoInitTask 已关闭==========");
      return;
    }
    logger.info("=========IndexInfoInitTask开始运行==========");
    if (!isEnabled())
      return;
    try {
      Long lastPsnId = startPsnId() == null ? 0L : startPsnId();
      try {
        // recommendService.upAppSettingConstant(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND,
        // 0);
        if (lastPsnId == 0) {
          recommendService.delAllFriendSysRecommendLog();
        }
        while (true) {
          if (isStop())
            return;
          List<Long> psnIdList = personManager.findPersonKnowByPsnId(lastPsnId, batchSize);
          if (CollectionUtils.isEmpty(psnIdList)) {
            systemRecommendService.removeAllRecommendScore();
            recommendService.upAppSettingConstant(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND, 1);
            recommendService.upAppSettingConstant(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND_START, 0);
            return;
          }
          lastPsnId = psnIdList.get(psnIdList.size() - 1);
          systemRecommendService.removeAllRecommendScore();
          systemRecommendLoginService.timingRecommendLogin(psnIdList);
          recommendService.upAppSettingConstant(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND_START, lastPsnId);
        }
      } catch (ServiceException e) {
        logger.error("好友智能推荐出错", e);
      }
    } catch (Exception e) {
      logger.error("好友智能推荐出错----------", e);
    }
  }

  private boolean isEnabled() {
    return AppSettingContext.getIntValue(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND) == 1;
  }

  private Long startPsnId() {
    return AppSettingContext.getLongValue(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND_START);
  }

  private boolean isStop() {
    return AppSettingContext.getIntValue(AppSettingConstants.SNS_AUTO_RECOMMEND_FRIEND_STOP) == 1;
  }
}
