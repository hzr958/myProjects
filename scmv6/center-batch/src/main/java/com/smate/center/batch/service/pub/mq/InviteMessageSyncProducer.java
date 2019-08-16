package com.smate.center.batch.service.pub.mq;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 收件箱-站内邀请异步处理MQ消息发送类.
 * 
 * @author maojianguo.
 * 
 */
@Component("snsInviteMessageProducer")
public class InviteMessageSyncProducer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InviteMessageSyncConsumer inviteMessageSyncConsumer;

  /**
   * 发送站内邀请的MQ消息.
   * 
   * @param mailParam 业务参数信息.
   * @param toNodeId MQ消息接收节点.
   * @param messageType
   */
  public void sendInviteMessageToSns(Map<String, Object> mailParam, Integer toNodeId, Integer messageType) {
    try {
      Assert.notNull(mailParam, "邀请参数map不能为空.");
      Integer fromNodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      SnsInviteMailMessage message = new SnsInviteMailMessage(fromNodeId, mailParam, messageType);
      this.inviteMessageSyncConsumer.receive(message);
    } catch (Exception e) {
      logger.error("发送站内请求失败！", e);
    }
  }
}
