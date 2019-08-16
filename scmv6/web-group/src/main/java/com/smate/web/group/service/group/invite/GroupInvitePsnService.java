package com.smate.web.group.service.group.invite;

import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.invit.GroupInvitePsn;


/**
 * 群组邀请人员服务类
 * 
 * @author tsz
 *
 */
public interface GroupInvitePsnService {

  /**
   * 检查群组对于 人 是否开放
   * 
   * @param form
   */
  public void checkGroupIsOpenForPsn(GroupPsnForm form);


  public GroupInvitePsn findGroupInvitePsn(Long groupId, Long psnId);

  /**
   * 查找群组成员成果信息列表，用于构建群组成果左边栏的成员成果项
   * 
   * @param form
   */
  public void findGroupInvitePsnForLeft(GroupPsnForm form);

  /**
   * 查找群组成员成果信息列表，用于构建群组成果左边栏的成员成果项(2016-10-10)
   * 
   * @param form
   */
  public void findGroupPubInvitePsnForLeft(GroupPsnForm form);
}
