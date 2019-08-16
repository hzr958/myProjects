package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;


/**
 * 请求加入群组和普通成员发送群组邀请的业务逻辑接口.
 * 
 * @author maojianguo
 * 
 */
public interface GroupInviteService {

  // 邀请类型(对应invite_mailbox表中的邀请类型invite_type参数值).
  final Integer GROUP_INVITE_TYPE = 2;
  // 邮件状态和邮件处理状态(对应invite_inbox表中的邮件状态status和邮件处理状态opt_status参数值).
  final Integer REQUEST_JOIN_INVITEIN_STATUS = 0;
  final Integer REQUEST_JOIN_INVITEIN_OPTSTATUS = 0;
  // 请求加入群组的邮件模板.
  final String REQUEST_JOIN_GROUP = "Request_Join_Group_Template";
  // 请求加入群组的邮件内容模板.
  final String REQUEST_JOIN_GROUP_CONTENT = "GroupJoin_Msg_Content_Template";
  // 请求加入群组的站内信标题模板.
  final String REQUEST_JOIN_GROUP_TITLE = "GroupJoin_Msg_Title_Template";
  // 请求加入群组的邮件标题模板
  final String REQUEST_JOIN_GROUP_MAIL_TITLE = "GroupJoin_Mail_Title_Template";
  // 请求加入群组邮件发送的收件人Map的key值.
  final String REQUEST_MAIL_RECEIVOR_KEY_PSNID = "psnId";
  final String REQUEST_MAIL_RECEIVOR_KEY_NAME = "name";
  final String REQUEST_MAIL_RECEIVOR_KEY_ENNAME = "enName";
  final String REQUEST_MAIL_RECEIVOR_KEY_EMAIL = "email";

  /**
   * 将邀请加入群组的成员列表的参数加以完善：增加查询成员的ID和email.
   * 
   * @param inviteList 邀请参数列表.
   * @return 匹配到信息的人员列表.
   */
  List<Map<String, String>> getInviteInfoList(List<Map<String, String>> inviteList);

  /**
   * 邀请加入群组消息.
   * 
   * @param list
   * @param inviteTitle
   * @param inviteBody
   * @param inviteBodyUrl
   * @throws ServiceException
   */
  void toSendInviteMsg(List<Map<String, String>> list, String inviteTitle, String inviteBody, String inviteBodyUrl)
      throws ServiceException;

  /**
   * 处理请求加入群组的站内邀请.
   * 
   * @throws ServiceException
   */
  void dealInviteSendMessage(GroupPsn groupPsn, GroupInvitePsn groupInvitePsn, Map<String, Object> ctxMap)
      throws ServiceException;

  /**
   * 处理请求加入群组的邮件发送逻辑.
   * 
   * @param param 邮件相关参数.
   * @param receivor 收件人相关参数.
   * @throws ServiceException
   */
  void sendGroupInviteMail(Map<String, Object> param, Map<String, Object> receivor) throws ServiceException;

  /**
   * 获取群组邀请的收件人.
   * 
   * @param param
   * @return
   */
  List<Map<String, Object>> getGroupReceivor(Map<String, Object> param);

  /**
   * 获取群组管理员的节点列表.
   * 
   * @param param
   * @return
   * @throws ServiceException
   */
  List<Integer> getAdminNodeList(Map<String, Object> param);

  /**
   * 获取群组邀请记录.
   * 
   * @param inviteId
   * @return
   * @throws ServiceException
   */
  GroupInvite getGroupInviteById(Long inviteId) throws ServiceException;

  /**
   * 根据邀请码获取群组邀请记录.
   * 
   * @param inviteCode 邀请码.
   * @return
   */
  GroupInvite getInviteByInviteCode(String inviteCode);

  /**
   * 插入邀请记录.
   * 
   * @param psnId
   * @param email
   * @param groupId
   * @param userId
   * @return
   * @throws ServiceException
   */
  GroupInvite saveInviteRecord(String psnId, String email, Long groupId, Long userId) throws ServiceException;
}
