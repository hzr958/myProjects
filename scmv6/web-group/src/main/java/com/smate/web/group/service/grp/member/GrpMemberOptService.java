package com.smate.web.group.service.grp.member;

import com.smate.web.group.action.grp.form.GrpMemberForm;

/**
 * 群组成员操作service
 * 
 * @author zzx
 */
public interface GrpMemberOptService {
  /**
   * 更新好友联系
   * 
   * @param form
   */
  void updateSelectedDate(GrpMemberForm form);

  /**
   * 记录推荐人员邀请记录
   */
  void recordMemberRcmdAccept(Long recommendPsnId, Long grpId, Integer isAccept);

  /**
   * 修改群组成员角色
   * 
   * @param form
   */
  void setGrpMemberRole(GrpMemberForm form);

  /**
   * 删除群组成员
   * 
   * @param form
   */
  void delGrpMember(GrpMemberForm form);

  /**
   * 处理群组申请
   * 
   * @param form
   */
  void disposeGrpApplication(GrpMemberForm form);

  /**
   * 处理群组邀请
   * 
   * @param form
   */
  void iviteGrpApplication(GrpMemberForm form);

  /**
   * 邀请加入群组
   * 
   * @param form
   */
  void invitedJoinGrp(GrpMemberForm form);

  /**
   * 通过邮件邀请加入群组
   * 
   * @param form
   */
  void invitedJoinGrpByEmail(GrpMemberForm form);

  /**
   * 申请加入群组
   * 
   * @param form
   */
  void applyJoinGrp(GrpMemberForm form);

  /**
   * 取消加入群组
   * 
   * @param form
   */
  void cancelJoinGrp(GrpMemberForm form);

  /**
   * 发送邀请邮件通知
   * 
   * @param form
   */
  void sendMessageForGrpInvited(GrpMemberForm form);

  /**
   * 发送申请邮件通知
   * 
   * @param form
   */
  void sendMessageForGrpApply(GrpMemberForm form);

  /**
   * 更新访问时间
   * 
   * @param psnId
   * @param grpId
   */
  void updateVisitDate(Long psnId, Long grpId);

  /**
   * 更新群组人员统计数
   * 
   * @param grpId
   */
  void updateGrpMemberCount(Long grpId);

  /**
   * 发送消息
   * 
   * @param form
   */
  public void sendMsg(GrpMemberForm form);

  void doRegosterBack(GrpMemberForm form) throws Exception;

  /**
   * 上传邮箱Excel解析处理
   * 
   * @param form
   * @throws Exception
   */
  void uploadEmailExcelTemp(GrpMemberForm form) throws Exception;

  /**
   * 创建邮箱Excel提供下载
   * 
   * @param form
   * @throws Exception
   */
  void downloadEmailExcelTemp(GrpMemberForm form) throws Exception;
}
