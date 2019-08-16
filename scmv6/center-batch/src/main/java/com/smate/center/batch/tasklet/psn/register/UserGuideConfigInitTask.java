package com.smate.center.batch.tasklet.psn.register;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.psn.register.UserGuideConfig;
import com.smate.center.batch.service.psn.register.UserGuideService;
import com.smate.center.batch.service.user.SysUserLoginLogService;
import com.smate.center.batch.service.user.UserService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/*
 * 用户在登录时，先查找缓存,如果没有,则查找数据库,再没有,则新建一份 初始化用户在新手指南的配置信息
 * 
 * 
 */

// TODO 此task迁移未完成，需要确认下具体实施的策略

public class UserGuideConfigInitTask extends BaseTasklet {

  @Autowired
  private UserGuideService userGuideService;
  @Autowired
  private UserService userService;
  @Autowired
  private SysUserLoginLogService sysUserLoginLogService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    Long sessionId = Long.parseLong(String.valueOf(jobContentMap.get("session_id")));
    String isFirst = String.valueOf(jobContentMap.get("is_first"));
    String loginType = String.valueOf(jobContentMap.get("login_type"));

    UserGuideConfig config = userGuideService.findUserGuideConfig(psnId);
    Integer first = 0;
    if (!"true".equals(isFirst)) {
      Date lastTime = sysUserLoginLogService.findLastTimeByPsnId(psnId);
      if (lastTime != null) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        if (lastTime.before(c.getTime())
        // TODO && !"1".equals(session.getAttribute(UPDATE_USER_GUIDE +
        // session.getId()))
        ) {
          // 上一次登录是在1个月前,则显示推荐成果或进入旧1,2,3步
          config.setShow(1L);
          config.setThirthDay(1);
        } else if (config.getShow().equals(1L) && config.getShowTimes().longValue() >= 3) {
          // 登陆时,如果上一次登陆在一个月内,且新手指南登录显示次数超过3次,就不再在登录时跳转到新手指南
          config.setShow(0L);
          config.setThirthDay(0);
          userGuideService.saveUserGuideConfig(config);
        }
      }
    } else {
      first = 1;
      if ("INSTEAD".equals(loginType)) {
        // 因为使用代登录模式,所以要添加登录日志
        userService.saveUserLoginLog(psnId, null, null);
      }
    }
    // 是否有成果认领
    Integer hasPub = 0;
    config.setHasPub(hasPub);
    config.setFirst(first);
    // TODO userGuideCache.put(USER_GUIDE_EHCACHE + session.getId(),
    // config);
  }

}
