package com.smate.center.open.service.group;

import com.smate.center.open.model.group.GroupPsn;


/**
 * 群组编辑业务逻辑接口
 * 
 * @author lhd
 *
 */
public interface GroupEditService {
  /**
   * 保存群组基本信息
   * 
   * @author lhd
   * @param groupPsn
   * @param psnId
   * @throws Exception
   */
  void saveGroupPsnInfo(GroupPsn groupPsn, Long psnId, Long openId) throws Exception;

  // /**
  // * 刷新群组头像.
  // * @author lhd
  // * @param groupImgUrl
  // * @return
  // */
  // String refreshRemoteAvatars(String groupImgUrl) throws Exception;

  /**
   * 保存群组关键字
   * 
   * @author lhd
   * @param groupPsn
   * @throws Exception
   */
  void saveGroupKeywordsInfo(GroupPsn groupPsn) throws Exception;

  /**
   * 保存人员与群众的邀请关系
   * 
   * @author lhd
   * @param groupPsn
   * @throws Exception
   */
  void saveGroupInvitePsnInfo(GroupPsn groupPsn, Long psnId) throws Exception;


}
