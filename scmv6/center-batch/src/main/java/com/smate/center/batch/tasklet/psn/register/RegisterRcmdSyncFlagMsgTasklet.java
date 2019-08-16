package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessage;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessageProducer;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 注册时冗余用户信息至RCMD
 * 
 */
public class RegisterRcmdSyncFlagMsgTasklet extends BaseTasklet {

  @Autowired
  private RcmdSyncFlagMessageProducer rcmdSyncFlagMessageProducer;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {

    Long psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
    message.setNameFlag(1);
    message.setWorkFlag(1);
    message.setKwztFlag(1);
    message.setEmailFlag(1);
    message.setExperienceFlag(1);
    message.setInsFlag(1);
    message.setEduFlag(1);
    message.setAttFlag(1);
    message.setContactFlag(1);
    rcmdSyncFlagMessageProducer.syncRcmdPsnInfoMessage(message);

  }


}
