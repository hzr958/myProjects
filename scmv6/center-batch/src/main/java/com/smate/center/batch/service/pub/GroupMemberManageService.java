package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.pub.GroupPsn;

/**
 * 群组人员管理(增删改)的业务逻辑接口.
 * 
 * @author maojianguo
 * 
 */
public interface GroupMemberManageService {

  // 通过请求加入群组的邮件标题模板
  final String REQUEST_JOIN_GROUP_APPR_MAIL_TITLE = "GroupJoin_Appr_Mail_Title_Template";

  /**
   * 同步群组信息到群组成员_MJG_SCM-1505.
   * 
   * @param groupId
   * @param groupInvitePsnList
   * @throws ServiceException
   */
  void toSendSyncGroupInvitePsn(Long groupId, List<GroupInvitePsn> groupInvitePsnList) throws ServiceException;

  public GroupInvitePsn toJoinInvite(Long groupId, Long userId, String isAccept) throws ServiceException;

  public String toApprove(Map<String, Object> ctxMap) throws ServiceException;

  public void toJoinApprove(Long invitePsnId, GroupPsn groupPsn) throws ServiceException;

  public String toRefuse(Long groupInviteId, Integer groupNodeId) throws ServiceException;

  /**
   * 批准加入群组.
   * 
   * @param map
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  String dealToJoinApprove(Map map) throws ServiceException;

  /**
   * 拒绝加入群组操作.
   * 
   * @param invitePsnId
   * @param groupNodeId
   * @return
   * @throws ServiceException
   */
  String toJoinRefuseApply(Long invitePsnId, Integer groupNodeId) throws ServiceException;

}
