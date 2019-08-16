package com.smate.center.batch.service.pub.mq;

import java.util.Map;

import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.MessageNoticeOutBox;
import com.smate.center.batch.model.mail.ReqMailBox;
import com.smate.center.batch.model.mail.ShareMailBox;

public class SnsSyncMailMessage {

  private Long receiverId;
  private InsideMailBox insideMailBox;
  private InviteMailBox inviteMailBox;
  private ReqMailBox reqMailBox;
  private ShareMailBox shareMailbox;
  private MessageNoticeOutBox messageNoticeOutBox;
  private Map<String, Object> mailParam;

  public SnsSyncMailMessage(Integer fromNode, InviteMailBox inviteMailBox, SnsSyncMailMsgActionEnum action) {
    this.inviteMailBox = inviteMailBox;
    this.action = action;
  }

  /**
   * 站内短消息.
   * 
   * @param recieverId
   * @param insideMailBox
   * @param mailParam
   * @param fromNode
   */
  public SnsSyncMailMessage(Long receiverId, InsideMailBox insideMailBox, Map<String, Object> mailParam,
      Integer fromNode) {
    this.receiverId = receiverId;
    this.insideMailBox = insideMailBox;
    this.mailParam = mailParam;
    this.action = SnsSyncMailMsgActionEnum.SYNC_INSIDE_ADD;
  }

  private SnsSyncMailMsgActionEnum action;

  public SnsSyncMailMessage() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * 站内通知
   * 
   * @param fromNode
   * @param notice
   * @param action
   */
  public SnsSyncMailMessage(Integer fromNode, MessageNoticeOutBox notice, SnsSyncMailMsgActionEnum action) {
    this.messageNoticeOutBox = notice;
    this.action = action;
  }

  public SnsSyncMailMessage(Integer fromNode, InsideMailBox insideMailBox, SnsSyncMailMsgActionEnum action) {
    this.insideMailBox = insideMailBox;
    this.action = action;
  }

  public SnsSyncMailMessage(Integer fromNode, ReqMailBox reqMailBox, SnsSyncMailMsgActionEnum action) {
    this.reqMailBox = reqMailBox;
    this.action = action;
  }

  public SnsSyncMailMessage(Integer fromNode, ShareMailBox shareMailbox, SnsSyncMailMsgActionEnum action) {
    this.shareMailbox = shareMailbox;
    this.action = action;
  }

  /**
   * @return the insideMailBox
   */
  public InsideMailBox getInsideMailBox() {
    return insideMailBox;
  }

  /**
   * @param insideMailBox the insideMailBox to set
   */
  public void setInsideMailBox(InsideMailBox insideMailBox) {
    this.insideMailBox = insideMailBox;
  }

  /**
   * @return the inviteMailBox
   */
  public InviteMailBox getInviteMailBox() {
    return inviteMailBox;
  }

  /**
   * @param inviteMailBox the inviteMailBox to set
   */
  public void setInviteMailBox(InviteMailBox inviteMailBox) {
    this.inviteMailBox = inviteMailBox;
  }

  /**
   * @return the reqMailBox
   */
  public ReqMailBox getReqMailBox() {
    return reqMailBox;
  }

  /**
   * @param reqMailBox the reqMailBox to set
   */
  public void setReqMailBox(ReqMailBox reqMailBox) {
    this.reqMailBox = reqMailBox;
  }

  /**
   * @return the shareMailbox
   */
  public ShareMailBox getShareMailbox() {
    return shareMailbox;
  }

  /**
   * @param shareMailbox the shareMailbox to set
   */
  public void setShareMailbox(ShareMailBox shareMailbox) {
    this.shareMailbox = shareMailbox;
  }

  /**
   * @return the action
   */
  public SnsSyncMailMsgActionEnum getAction() {
    return action;
  }

  /**
   * @param action the action to set
   */
  public void setAction(SnsSyncMailMsgActionEnum action) {
    this.action = action;
  }

  public MessageNoticeOutBox getMessageNoticeOutBox() {
    return messageNoticeOutBox;
  }

  public void setMessageNoticeOutBox(MessageNoticeOutBox messageNoticeOutBox) {
    this.messageNoticeOutBox = messageNoticeOutBox;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public Map<String, Object> getMailParam() {
    return mailParam;
  }

  public void setMailParam(Map<String, Object> mailParam) {
    this.mailParam = mailParam;
  }
}
