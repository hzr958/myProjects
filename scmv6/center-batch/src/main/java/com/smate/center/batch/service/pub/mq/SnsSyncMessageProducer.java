package com.smate.center.batch.service.pub.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.MessageNoticeOutBox;
import com.smate.center.batch.model.mail.ReqMailBox;
import com.smate.center.batch.model.mail.ShareMailBox;
import com.smate.core.base.utils.security.SecurityUtils;


@Component("snsSyncMessageProducer")
public class SnsSyncMessageProducer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SnsSyncMessageConsumer snsSyncMessageConsumer;

  /**
   * 站内通知同步
   * 
   * @param dynReply
   * @param nodeId
   * @throws ServiceException
   * @author yangpeihai
   */
  @SuppressWarnings("unchecked")
  public void syncMessageNoticeToSns(MessageNoticeOutBox notice, Integer nodeId) throws ServiceException {

    try {
      Assert.notNull(notice, "站内通知不能为空.");
      SnsSyncMailMessage message = new SnsSyncMailMessage(nodeId, notice, SnsSyncMailMsgActionEnum.SYNC_MSGNOTICE_ADD);
      this.snsSyncMessageConsumer.receive(message);
    } catch (Exception e) {
      logger.error("站内通知同步失败！", e);
      throw new ServiceException();

    }

  }

  /**
   * 站内短消息同步.
   * 
   * @param receiverId
   * @param insideMailBox
   * @param mailParam
   * @param toNodeId
   * @throws ServiceException
   */
  public void syncInsideMsgToSns(Long receiverId, InsideMailBox insideMailBox, Map<String, Object> mailParam,
      Integer toNodeId) throws ServiceException {

    try {
      Assert.notNull(receiverId, "站内短消息收件人不恩功能为空．");
      Assert.notNull(insideMailBox, "站内短消息不能为空.");
      SnsSyncMailMessage message =
          new SnsSyncMailMessage(receiverId, insideMailBox, mailParam, SecurityUtils.getCurrentAllNodeId().get(0));
      this.snsSyncMessageConsumer.receive(message);
    } catch (Exception e) {
      logger.error("发送短消息同步失败！", e);
      throw new ServiceException();
    }

  }

  /**
   * 站内邀请消息同步
   * 
   * @param mailMsg
   * @param nodeId
   * @throws ServiceException
   */
  public void syncInviteMsgToSns(InviteMailBox mailMsg, Integer nodeId) throws ServiceException {

    try {
      Assert.notNull(mailMsg, "站内短消息不能为空.");
      SnsSyncMailMessage message = new SnsSyncMailMessage(nodeId, mailMsg, SnsSyncMailMsgActionEnum.SYNC_INVITE_ADD);
      this.snsSyncMessageConsumer.receive(message);
    } catch (Exception e) {

      logger.error("发送短消息同步失败！", e);
      throw new ServiceException();

    }

  }

  /**
   * 站内请求更新消息同步
   * 
   * @param mailMsg
   * @param nodeId
   * @throws ServiceException
   */
  public void syncReqMsgToSns(ReqMailBox mailMsg, Integer nodeId) throws ServiceException {

    try {
      Assert.notNull(mailMsg, "站内短消息不能为空.");
      SnsSyncMailMessage message = new SnsSyncMailMessage(nodeId, mailMsg, SnsSyncMailMsgActionEnum.SYNC_REQ_ADD);
      this.snsSyncMessageConsumer.receive(message);
    } catch (Exception e) {

      logger.error("发送请求更新短消息同步失败！", e);
      throw new ServiceException();

    }

  }

  /**
   * 站内共享消息同步
   * 
   * @param mailMsg
   * @param nodeId
   * @throws ServiceException
   */
  public void syncShareMsgToSns(ShareMailBox mailMsg, Integer nodeId) throws ServiceException {

    try {
      Assert.notNull(mailMsg, "站内共享消息不能为空.");
      SnsSyncMailMessage message = new SnsSyncMailMessage(nodeId, mailMsg, SnsSyncMailMsgActionEnum.SYNC_SHARE_ADD);
      this.snsSyncMessageConsumer.receive(message);
    } catch (Exception e) {

      logger.error("发送请求更新短消息同步失败！", e);
      throw new ServiceException();

    }

  }

}
