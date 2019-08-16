package com.smate.center.open.service.group.member;

import com.smate.center.open.model.group.GroupInvitePsn;

/**
 * 群组成员相关信息检索的业务逻辑接口
 * 
 * @author lhd
 *
 */
public interface GroupMemberService {

  /**
   * 构建人员信息到群组人员关系表
   * 
   * @author lhd
   * @param groupInvitePsn
   * @return
   * @throws Exception
   */
  GroupInvitePsn buildGroupInvitePsn(GroupInvitePsn groupInvitePsn) throws Exception;

}
