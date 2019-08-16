package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.psn.InvitePsnValidate;
import com.smate.center.task.single.service.person.InvitePsnValidateMailService;

public class SendInvitePsnValidateMailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InvitePsnValidateMailService invitePsnValidateMailService;

  public SendInvitePsnValidateMailTask() {
    super();
  }

  public SendInvitePsnValidateMailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SendPubAssignEmailTask已关闭==========");
      return;
    }
    List<InvitePsnValidate> psnList = invitePsnValidateMailService.getInvitePsnValidate();
    if (psnList.size() == 0) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
      }
    }
    for (InvitePsnValidate invitePsnValidate : psnList) {
      try {
        Integer result = invitePsnValidateMailService.sendInviteEmailToPsn(invitePsnValidate);
        invitePsnValidateMailService.updateSendStatus(invitePsnValidate.getPsnId(), result);
      } catch (Exception e) {
        logger.error("发送邀请订购科研验证服务失败-----psnId:" + invitePsnValidate.getPsnId(), e);
        invitePsnValidateMailService.updateSendStatus(invitePsnValidate.getPsnId(), 9);
      }
    }
  }
}
