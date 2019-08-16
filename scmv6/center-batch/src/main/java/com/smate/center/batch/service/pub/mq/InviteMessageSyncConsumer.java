package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.mail.InvitationBusinessService;

/**
 * 收件箱-站内邀请异步处理MQ消息接收类.
 * 
 * @author maojianguo
 * 
 */
@Component("inviteMessageSyncConsumer")
public class InviteMessageSyncConsumer {

  @Autowired
  private InvitationBusinessService invitBusinessService;

  /**
   * 接收处理站内邀请的MQ消息.
   */
  public void receive(SnsInviteMailMessage message) throws ServiceException {

    if (message instanceof SnsInviteMailMessage) {
      SnsInviteMailMessage inviteMessage = (SnsInviteMailMessage) message;
      invitBusinessService.dealInviteMessage(inviteMessage);

    }
  }

}
