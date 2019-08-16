package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.PsnKnowCopartner;
import com.smate.center.batch.model.psn.PsnKnowWorkEdu;

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

  public void sendReqAddFrdMail(Map<String, ?> ctxMap, Long mailType) throws ServiceException;

  void deletePsnKnowWorkEdu(PsnKnowWorkEdu psnKnowWorkEdu) throws ServiceException;

  void deletePsnKnowCopartner(PsnKnowCopartner psnKnowCopartner) throws ServiceException;

  List<PsnKnowWorkEdu> getPsnKnowWorkEdu(Long psnId) throws ServiceException;

  List<PsnKnowCopartner> getPsnKnowCopartner(Long psnId) throws ServiceException;
}
