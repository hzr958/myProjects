package com.smate.web.psn.service.friend;



/**
 * 好友相关邮件发送的业务逻辑接口.
 * 
 * @author maojianguo
 * 
 */
public interface FriendMailService {

  /**
   * 发送请求加为好友的邮件.
   * 
   * @param ctxMap
   * @param mailType
   * @throws ServiceException
   * 
   */

  // 通过邮件邀请人员加入群组的邀请码生成规则.
  static final String INVITATION_PARAM_INVITEID = "des3InviteId";
  static final String INVITATION_PARAM_NODEID = "nodeId";
  static final String INVITATION_PARAM_MAILID = "des3mailId";
  static final String INVITATION_PARAM_INBOXID = "des3inboxId";
  static final String INVITATION_PARAM_PSNID = "des3PsnId";
  // 邀请人员加为好友的类型标示.
  static final String FRIEND_INVITE_KEY = "frd_invite";

  static final String FRIEND_INVITATION_CODE_RULE = INVITATION_PARAM_INVITEID + "-" + INVITATION_PARAM_NODEID + "-"
      + INVITATION_PARAM_MAILID + "-" + INVITATION_PARAM_INBOXID + "-" + INVITATION_PARAM_PSNID;


}
