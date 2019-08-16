package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.center.batch.model.sns.pub.PersonEmail;
import com.smate.core.base.utils.model.Page;


/**
 * 群组成员相关信息检索的业务逻辑接口.
 * 
 * @author maojianguo
 * 
 */
public interface GroupMemberService {

  /**
   * 获取群组成员列表信息.
   * 
   * @param groupId
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<GroupInvitePsn> findGroupMember(Long groupId, Page<GroupInvitePsn> page, String searchKey)
      throws ServiceException;

  /**
   * 获取群组成员列表信息，包括群组成员和请求加入成员.
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  List<GroupInvitePsn> findGroupMemberByAll(Long groupId) throws ServiceException;

  /**
   * 查找群组所有成员.
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  List<GroupInvitePsn> findGroupMember(Long groupId) throws ServiceException;

  /**
   * 结合查询限制条件检索匹配用户.
   * 
   * @param inviteList 邀请参数列表.
   * @return 匹配到信息的人员列表.
   */
  List<Map<String, String>> getInvitePsnList(List<Map<String, String>> inviteList, Map<String, Object> configParam);

  /**
   * 拷贝人员信息到群组人员关系表.
   * 
   * @param gip 群组人员.
   * @return
   * @throws ServiceException
   */
  GroupInvitePsn copyGroupMemberDetail(GroupInvitePsn gip) throws ServiceException;

  /**
   * 根据ID检索人员记录.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  GroupMember findGroupMemberDetail(Long psnId) throws ServiceException;

  /**
   * 获取群组管理员的节点列表.
   * 
   * @param groupId
   * @return
   * @throws DaoException
   * @throws ServiceException
   */
  List<Integer> findAdminNodeListByGroupId(Long groupId) throws ServiceException;

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
   * 按照检索条件检索匹配用户.
   * 
   * @param paramInfo 检索条件.
   * @return
   * @throws DaoException
   */
  List<Map<String, String>> getMatchedUser(Map<String, Object> paramInfo) throws DaoException;

  /**
   * 获取群组成员PSN_ID列表.
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  List<Long> findGroupMemberPsnId(List<GroupInvitePsn> groupMemberList) throws ServiceException;

  /**
   * 群组成员列表部分字段进行拼接，追加好友数和评论数.
   * 
   * @param groupMemberPsnIdList
   * @param tempGroupInvitePsnList
   * @return
   * @throws ServiceException
   */
  List<GroupInvitePsn> groupMemberPage(List<Long> groupMemberPsnIdList, List<GroupInvitePsn> tempGroupInvitePsnList)
      throws ServiceException;

  /**
   * 获取群组成员记录.
   * 
   * @param psnId
   * @param groupId
   * @return
   */
  GroupInvitePsn getGroupInvitePsn(Long psnId, Long groupId);

  /**
   * 获取用户在群组中的身份(是否群组管理员).
   * 
   * @param psnId
   * @param groupId
   * @return true-是；false-不是.
   */
  boolean isGroupManager(Long psnId, Long groupId);
}
