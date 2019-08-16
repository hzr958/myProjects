package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.PersonEmail;


/**
 * 群组-管理员邀请人员加入群组的业务逻辑接口.
 * 
 * @author MaoJianGuo.
 * 
 */
public interface GroupAdminInviteService extends Serializable {

  // 邀请人员加入群组的Map中各类型人员key值.
  public static final String INVITE_MAP_KEY_MEM = "group_member";
  public static final String INVITE_MAP_KEY_APPROV = "approve_member";
  public static final String INVITE_MAP_KEY_INVITE = "invite_psn";
  public static final String INVITE_MAP_KEY_JOIN = "invite_map_key_join";

  // 邀请成员加入群组的类型标示.
  static final String GROUP_INVITE_KEY = "group_invite";
  static final String GROUP_INVITE_REPEAT_NOTE = "group";
  static final Integer GROUP_EXISTS_NONE = 2;
  // 群组人员邀请类别
  static final Integer GROUP_INVITE_TYPE = 1;
  // 是否同意加入群组[2=需要普通成员确认,1=已确认,0=否,空=需要管理员确认]
  static final String ACCEPT_BY_USER = "2";
  // 通过邮件邀请人员加入群组的邀请码生成规则.
  static final String INVITATION_PARAM_INVITEID = "des3InviteId";
  static final String INVITATION_PARAM_NODEID = "des3NodeId";
  static final String INVITATION_PARAM_MAILID = "des3mailId";
  static final String INVITATION_PARAM_INBOXID = "des3inboxId";
  static final String GROUP_INVITATION_CODE_RULE = INVITATION_PARAM_INVITEID + "-" + INVITATION_PARAM_NODEID + "-"
      + INVITATION_PARAM_MAILID + "-" + INVITATION_PARAM_INBOXID;
  // 邀请加入群组的邮件模板(系统内用户).
  static final String INVITE_ATTEND_GROUP = "Invite_Attend_Group_Template";
  // 邀请加入群组的邮件模板(系统外用户).
  static final String INVITE_ATTEND_GROUP_OUT = "Invite_Outside_Attend_Group_Template";

  /**
   * 判断邮箱地址是否已验证的首要邮件.
   * 
   * @param psnId 人员ID.
   * @param email 邮箱地址.
   * @return
   * @throws ServiceException
   */
  boolean isPsnEmailVerified(Long psnId, String email) throws ServiceException;

  /**
   * 通过邮件地址获取用户邮件列表.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  List<PersonEmail> findListByEmail(String email) throws ServiceException;

  /**
   * 获取群组的管理员.
   * 
   * @param groupId 群组ID.
   * @return
   */
  List<Map<String, Object>> findGroupAdminsInfo(Long groupId);

  /**
   * 处理群组邀请的列表.
   * 
   * @param groupPsn
   * @param groupPsnForm
   * @param inviteList
   * @return
   * @throws ServiceException
   */
  // Map<String, Object> dealInviteOperat(GroupPsn groupPsn, GroupPsnForm groupPsnForm,
  // List<Map<String, String>> inviteList) throws ServiceException;

  /**
   * 增加邀请人员加入群组的接口，对业务逻辑接口的参数进行封装.
   * 
   * @param tmpList 被邀请人员.
   * @param groupRecord 群组记录.
   * @param map 传递参数.
   * @throws ServiceException
   */
  void toSendGroupInvite(List<Map<String, String>> tmpList, GroupPsn groupPsn, Map<String, Object> map)
      throws ServiceException;

  /**
   * 处理消息中心的群组邀请.
   * 
   * @param mailBox 发件箱记录.
   * @param inbox 收件箱记录.
   * @param currentPsnId 收件人.
   * @param inviteId 群组邀请记录ID.
   * @param nodeId 节点ID.
   * @return 处理结果.
   * @throws ServiceException
   */
  Integer dealGroupInvite(InviteMailBox mailBox, InviteInbox inbox, Long currentPsnId, Long inviteId, Integer nodeId)
      throws ServiceException;

  /**
   * 发送群组加入通知SCM-5442
   */
  void toSendGroupJoinMail(List<Map<String, String>> tmpList, GroupPsn groupPsn, String casUrl) throws ServiceException;
}
