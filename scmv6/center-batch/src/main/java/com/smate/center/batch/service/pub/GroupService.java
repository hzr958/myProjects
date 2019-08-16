package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.center.batch.model.sns.pub.GroupFundInfo;
import com.smate.center.batch.model.sns.pub.GroupFundInfoMembers;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.tmp.pdwh.PubFundingInfo;

/**
 * 群组接口.
 * 
 * @author zhuagnyanming
 * 
 */
public interface GroupService extends Serializable {
  final String GROUP_CATEGORY = "group";
  final String GROUP_CATEGORY_CREATE = "1";// 我创建的群组
  final String GROUP_CATEGORY_JOIN = "0";// 我参与的群组

  final String GROUP_ROLE_CREATOR = "1";
  final String GROUP_ROLE_ADMIN = "2";
  final String GROUP_ROLE_MEMBER = "3";

  // 公开类型[O=开放,H=半开放,P=保密]
  final String FULL_OPEN = "O";
  final String HALF_OPEN = "H";
  final String NOT_OPEN = "P";

  // 是否已经激活[1=激活,0=未激活]
  final String ACTIVITY = "1";
  final String NOT_ACTIVITY = "0";

  // 是否同意加入群组[2=需要普通成员确认,1=已确认,0=否,空=需要管理员确认]
  final String ACCEPT_BY_USER = "2";
  final String ACCEPT = "1";
  final String NOT_ACCEPT = "0";

  // 删除状态
  final String STATUS99 = "99";
  // 正常状态
  final String STATUS01 = "01";

  // 讨论话题拥有者或回复拥有者
  final String DISCUSS_OWNER = "1";

  /**
   * 获取群组个人信息.
   * 
   * @param groupId
   * @return
   * @throws ServiceException @
   */
  public GroupInvitePsn findGroupInvitePsn(Long groupId) throws ServiceException;

  /**
   * 获取群组个人信息.
   * 
   * @param groupId
   * @param isActivity
   * @return
   * @throws ServiceException
   */
  public GroupInvitePsn findGroupInvitePsn(Long groupId, boolean isActivity) throws ServiceException;

  /**
   * 查询群组信息.
   * 
   * @param groupId
   * @return @
   */
  public GroupPsn getGroupPsnInfo(Long groupId);

  /**
   * 同步群组信息到群组成员.
   * 
   * @param groupId
   * @throws ServiceException
   */
  public void toSendSyncGroupInvitePsn(Long groupId);

  /**
   * 判断人是否在成果所在的群组中.
   * 
   * @param psnId
   * @param pubId
   * @return
   * @throws ServiceException
   */
  boolean checkPsnIsInPubGroup(Long psnId, Long pubId) throws ServiceException;

  public GroupPsnNode findGroup(Long groupId) throws ServiceException;

  /**
   * 查询群组人员临时邀请表.
   * 
   * @param inviteId
   * @return
   * @throws ServiceException
   */
  public GroupInvite getGroupInviteById(Long inviteId) throws ServiceException;

  /**
   * 查询人员信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public GroupMember findGroupMemberDetail(Long psnId) throws ServiceException;

  /**
   * 检查邀请链接是否有效.
   * 
   * @param inviteId
   * @return
   * @throws ServiceException
   */
  public boolean checkInviteIsValid(Long inviteId) throws ServiceException;

  public List<GroupFundInfo> getGroupFundInfo(Integer size, Long startGroupId, Long endGroupId);

  public List<PubFundingInfo> getPubFundingInfoByFundingNo(String fundingNo);

  public void importPdwhPubIntoGroup(Long groupId, Long psnId, Long pubId, Integer dbId) throws Exception;

  public void saveOpResult(GroupFundInfo groupInfo, Integer status);

  List<GroupFundInfoMembers> getGroupFundInfoMembers(Integer size, Long startGroupId, Long endGroupId);

  void saveOpResult(GroupFundInfoMembers groupFundInfoMembers, Integer status);

  void handleGroupMemberInfo(GroupFundInfoMembers groupFundInfoMembers) throws Exception;

  void insertIntoRcmdPdwh(Long groupId, Long pdwhId, Integer publishYear) throws Exception;
}
