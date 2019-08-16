package com.smate.web.group.service.grp.member;

import com.smate.web.group.action.grp.form.GrpMemberForm;

/**
 * 群组权限以及业务判断service
 * 
 * @author zzx
 */
public interface GrpRoleService {
  /**
   * 获取群组角色
   * 
   * @param form
   * @return
   */
  Integer getGrpRole(Long psnId, Long grpId);

  /**
   * 获取群组角色（移除群组成员专用）：当前人员权限必须为管理员及以上，且操作人权限高与被移除成员的权限
   * 
   * @param form
   * @return
   */
  boolean getGrpRoleForDelMember(GrpMemberForm form);

  /**
   * 判断是否能访问群组详情
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  boolean checkRoleVisitGrp(Long psnId, Long grpId) throws Exception;

  /**
   * 判断群组是否存在
   * 
   * @param grpId
   * @return
   */
  boolean IsisExistGrp(Long grpId);

  boolean isAutoExit(GrpMemberForm form) throws Exception;
}
