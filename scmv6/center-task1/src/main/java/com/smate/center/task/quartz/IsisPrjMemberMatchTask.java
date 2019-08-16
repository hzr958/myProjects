package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.MatchTaskException;
import com.smate.center.task.model.sns.quartz.GroupFundInfoMembers;
import com.smate.center.task.service.sns.quartz.IsisPrjMemberMatchService;
import com.smate.center.task.service.sns.quartz.TaskSchedulingService;

public class IsisPrjMemberMatchTask extends TaskAbstract {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  /**
   * 根据GroupId范围查询DAO层暂未使用
   */
  private Long startGroupId = 0L;

  private Long endGroupId = 300000001208000L;

  @Autowired
  private IsisPrjMemberMatchService isisPrjMemberMatchService;


  @Autowired
  TaskSchedulingService taskSchedulingService;


  public IsisPrjMemberMatchTask() {}

  public IsisPrjMemberMatchTask(String beanName) {
    super(beanName);
  }


  public void Run() throws MatchTaskException {

    try {
      logger.info("---------IsisPrjMemberMatchTask----------");
      // 任务逻辑控制开关
      if (!super.isAllowExecution()) {
        logger.info("=========成员匹配任务已关闭==========");
        return;
      }
      /*
       * if (isRun()==false) { logger.info("=========成员匹配任务已关闭=========="); return; }
       */

      logger.info("===========成员匹配任务开始执行======");
      List<GroupFundInfoMembers> groupList =
          isisPrjMemberMatchService.getGroupFundInfoMembers(SIZE, startGroupId, endGroupId);

      if (CollectionUtils.isEmpty(groupList)) {
        logger.info("==================IsisPrjMemberMatchTask GROUP_FUNDINFO表待处理数据为空!!=========, time = " + new Date());
        return;
      }

      for (GroupFundInfoMembers groupFundInfoMembers : groupList) {
        try {
          this.isisPrjMemberMatchService.handleGroupMemberInfo(groupFundInfoMembers);
          // 处理完成更新状态
          this.isisPrjMemberMatchService.saveOpResult(groupFundInfoMembers, 1);
        } catch (Exception e) {
          logger.error("IsisPrjMemberMatchTask出错, groupId = " + groupFundInfoMembers.getGroupId(), e);
          // 错误处理，改变状态位为9
          this.isisPrjMemberMatchService.saveOpResult(groupFundInfoMembers, 9);
        }
      }

    } catch (Exception e) {
      logger.error("IsisPrjMemberMatchTask,运行异常", e);
    }

  }
  /*
   * public boolean isRun() throws BatchTaskException{ //任务开关控制逻辑 // return true; return
   * taskSchedulingService.getApplicationQuartzSettingValue("isisPrjMemberMatchTaskTrigger") == 1; }
   */

}
