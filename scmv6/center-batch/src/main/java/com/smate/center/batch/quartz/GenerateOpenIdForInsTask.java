package com.smate.center.batch.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.OpenUserCreateByIns;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.psn.PsnOpenIdCreateService;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

public class GenerateOpenIdForInsTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  // 详见OpenConsts，后台任务自动生成
  public static final int OPENID_CREATE_TYPE_5 = 5;

  @Autowired
  private PsnOpenIdCreateService psnOpenIdCreateService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  public void run() throws BatchTaskException {
    logger.debug("====================================generateOpenIdForInsTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================generateOpenIdForInsTask===开关关闭");
      return;
    } else {
      try {

        doRun();

      } catch (BatchTaskException e) {
        logger.error("generateOpenIdForInsTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // 选择状态为1的单位
    List<OpenUserCreateByIns> insList = this.psnOpenIdCreateService.getInsList(1);
    if (insList == null) {
      logger.debug("===generateOpenIdForInsTask===V_OPEN_USER_CREATE_BY_INS表中获取数据未空");
      return;
    }

    for (OpenUserCreateByIns ins : insList) {
      Long insId = ins.getInsId();
      String token = ins.getToken();

      if (insId != null && token != null) {

        List<SysRolUser> userList = this.psnOpenIdCreateService.getPsnIdByInsId(insId);
        for (SysRolUser user : userList) {
          Long psnId = user.getPsnId();
          try {
            this.psnOpenIdCreateService.generateOpenId(token, psnId, OPENID_CREATE_TYPE_5);
          } catch (Exception e) {
            logger.debug("===generateOpenIdForInsTask===创建Token错误, psnId = " + psnId, e);
            continue;
          }
        }
      }
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("generateOpenIdForInsTask") == 1;
    // return false;
  }
}
