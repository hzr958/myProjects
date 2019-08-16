package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.mail.InsideMessageService;

@Component("snsSyncMessageConsumer")
public class SnsSyncMessageConsumer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MessageService messageService;
  @Autowired
  private InsideMessageService insideMessageService;

  public void receive(SnsSyncMailMessage message) throws ServiceException {

    Assert.notNull(message);
    SnsSyncMailMsgActionEnum action = message.getAction();
    try {

      logger.info("接受到一条站内短信同步消息，{}", message);

      if (action == SnsSyncMailMsgActionEnum.SYNC_INSIDE_ADD) {

        insideMessageService.saveSyncInsideMessageAndSendMail(message.getReceiverId(), message.getInsideMailBox(),
            message.getMailParam());

      } else if (action == SnsSyncMailMsgActionEnum.SYNC_INVITE_ADD) {

        messageService.saveSyncInviteMessage(message.getInviteMailBox());

      } else if (action == SnsSyncMailMsgActionEnum.SYNC_REQ_ADD) {

        messageService.saveSyncReqMessage(message.getReqMailBox());
      } else if (action == SnsSyncMailMsgActionEnum.SYNC_SHARE_ADD) {
        messageService.saveSyncShareMessage(message.getShareMailbox());

      } else if (action == SnsSyncMailMsgActionEnum.SYNC_MSGNOTICE_ADD) {
        messageService.saveSynNoticeMessage(message.getMessageNoticeOutBox());

      }

    } catch (Exception e) {
      logger.error("同步站内短消息失败！", e);
    }
  }

}
