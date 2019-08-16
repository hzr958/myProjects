package com.smate.center.batch.service.group;

import com.smate.center.batch.model.sns.pub.GroupPsn;


/**
 * 群组信息处理接口
 * 
 * @author zzx
 *
 */
public interface SyncGroupService {
  /**
   * 不存在的单位.
   */
  final Long NOTEXIST_INSID = -1L;

  /**
   * 同步群组信息到群组成员
   * 
   * @param groupPsn
   * @throws ServiceException
   */
  void syncGroupInfoToGroupPsn(Long groupId) throws Exception;

  /**
   * 同步群组信息
   * 
   * @param groupId
   * @throws Exception
   */
  void syncGroupPsnToSns(Integer nodeId, GroupPsn groupPsn) throws Exception;

  /**
   * 同步群组成员（传递GroupPsn对象和invite_psn_id）
   * 
   * @param invitePsnId
   * @throws Exception
   */
  void syncGroupInvitePsnToSns(Long invitePsnId, GroupPsn groupPsn) throws Exception;

  /**
   * 群组信息和成员有变动，同步到ROL(合作分析)(传递GroupPsn对象)
   * 
   * @param groupPsn
   * @throws Exception
   */
  void syncForAllGroupUpdateToRol(GroupPsn groupPsn) throws Exception;

}
