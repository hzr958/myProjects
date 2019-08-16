package com.smate.web.dyn.service.msg;

import java.util.Map;

import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;

/**
 * 消息显示服务接口
 * 
 * @author zzx
 *
 */
public interface ShowMsgService {
  /**
   * 获取消息
   * 
   * @param form
   */
  void getMsgInfo(MsgShowForm form) throws Exception;

  /**
   * 获取未读消息
   * 
   * @param form
   */
  Map<String, String> countUnreadMsg(MsgShowForm form) throws Exception;

  /**
   * 获取读消息
   * 
   * @param form
   */
  Map<String, String> countReadMsg(MsgShowForm form) throws Exception;

  /**
   * 获取会话数量
   * 
   * @param form
   */
  Map<String, String> countChatPsn(MsgShowForm form) throws Exception;

  /**
   * 发送消息
   * 
   * @param form
   */
  void sendMsg(MsgShowForm form) throws Exception;

  /**
   * 删除消息
   * 
   * @param form
   */
  void delMsg(MsgShowForm form) throws Exception;

  /**
   * 删除消息
   * 
   * @param form
   */
  void delChatMsg(MsgShowForm form) throws Exception;

  /**
   * 标记消息为已读
   * 
   * @param form
   */
  void setReadMsg(MsgShowForm form) throws Exception;

  /**
   * 获取聊天会话列表
   * 
   * @param form
   */
  void getChatPsnList(MsgShowForm form) throws Exception;

  /**
   * 
   * @param form
   * @throws Exception
   */
  @Deprecated
  void optFulltextRequest(MsgShowForm form) throws Exception;

  /**
   * 删除会话
   * 
   * @param form
   * @throws Exception
   */
  void delMsgChatRelation(MsgShowForm form) throws Exception;

  /**
   * 获取站内信聊天某个人的第一条聊天记录
   * 
   * @param form
   * @throws Exception
   */
  void getChatPsnLastRecord(MsgShowForm form) throws Exception;

  void updatePsnRecent(Long senderId, Long recevierId) throws Exception;

  void createMsgChat(MsgShowForm form) throws Exception;

  void menuMsgPrompt(MsgShowForm form) throws Exception;

  /**
   * 移动端-获取消息
   * 
   * @param form
   * @throws Exception
   */
  void getMobileMsgInfo(MobileMsgShowForm form) throws Exception;

  /**
   * 移动端-设置消息为已读
   * 
   * @param form
   * @throws Exception
   */
  public int mobileSetReadMsg(MobileMsgShowForm form) throws Exception;

  /**
   * 移动端-删除消息
   * 
   * @param form
   * @throws Exception
   */
  void mobileDelMsg(MobileMsgShowForm form) throws Exception;

  /**
   * 移动端-获取未读消息
   * 
   * @param form
   * @throws Exception
   */
  public Long mobileCountUnreadMsg(MobileMsgShowForm form) throws Exception;

  Long getPubConfirmCount(Long psnId);

  Long getTempPsnCount(Long psnId);

  Long getFulltextCount(Long psnId);

  /**
   * App发送站内信聊天信息
   * 
   * @param form
   * @throws Exception
   */
  void appSendMsg(MsgShowForm form) throws Exception;

  void getSearchChatPsnList(MsgShowForm form) throws Exception;

  void showChatUI(MsgShowForm form) throws Exception;

  void showNewChatUI(MsgShowForm form) throws Exception;

  /**
   * 发送站内信消息 邮件 文件，文本，成果
   * 
   * @param form
   */
  public void sendInsideMsgEmail(MsgShowForm form);

  /**
   * 分享项目时发送邮件进行通知
   * 
   * @param form
   */
  public void sendPrjShareEmail(MsgShowForm form);

  /**
   * 添加文件分享记录
   * 
   * @param form
   * @throws Exception
   */
  public void addPsnFileShareRecord(MsgShowForm form) throws Exception;

  /**
   * 构建接收者信息，将表单中传递的email进行构建，判断邮件是否合法，判断当前邮件是否为站内人员的邮箱
   * 
   * @param form
   */
  void buildReceivers(MsgShowForm form);

}
