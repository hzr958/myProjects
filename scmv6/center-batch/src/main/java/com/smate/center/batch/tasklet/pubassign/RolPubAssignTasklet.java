package com.smate.center.batch.tasklet.pubassign;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.LocalQueueMessage;
import com.smate.center.batch.service.pub.mq.BaseLocalQueneMessage;
import com.smate.center.batch.service.pub.mq.LocalQueueService;
import com.smate.center.batch.service.pub.mq.PubAssignMessage;
import com.smate.center.batch.service.rol.pub.PubRolAssignService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.center.batch.util.pub.JsonUtils;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 机构版，成果指派至个人
 * 
 * 
 */
public class RolPubAssignTasklet extends BaseTasklet {

  @Autowired
  private LocalQueueService localQueueService;

  @Autowired
  private PubRolAssignService pubRolAssignService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {

    Long msgId = Long.parseLong(String.valueOf(withData));
    LocalQueueMessage msg = this.localQueueService.getMsgByMsgId(msgId);
    if (msg == null) {
      return DataVerificationStatus.NULL;
    }

    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {

    String msgIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long msgId = Long.parseLong(msgIdStr);
    LocalQueueMessage msg = this.localQueueService.getMsgByMsgId(msgId);

    if (msg == null)
      return;

    try {
      String msgBody = msg.getMsgBody();
      String msgClaz = msg.getMsgClz();
      JSONObject tmpl = JSONObject.fromObject(msgBody, JsonUtils.configJson(null, "yyyy-MM-dd HH:mm:ss"));
      BaseLocalQueneMessage message = (BaseLocalQueneMessage) JSONObject.toBean(tmpl, Class.forName(msgClaz));
      this.pubRolAssignService.doAssignPub((PubAssignMessage) message);
      this.localQueueService.deleteMsg(msgId);

    } catch (Exception e) {
      logger.error("本地消息发送错误，消息ID：" + msgId + "，消息bean:" + msg.getBeanName() + "详情见消息错误表");
      localQueueService.remarkError(msgId, e);
      throw new BatchTaskException(e);
    }
  }



}
