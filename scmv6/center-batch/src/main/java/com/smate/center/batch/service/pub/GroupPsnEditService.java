package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.GroupBaseInfo;
import com.smate.center.batch.model.sns.pub.GroupControl;
import com.smate.center.batch.model.sns.pub.GroupFilter;
import com.smate.center.batch.model.sns.pub.GroupKeyDisc;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupStatistics;

/**
 * 群组编辑服务(原group_psn表拆分为多个表，为减少对其他模块的影响，增加此服务以便数据封装)_SCM-6000.
 * 
 * @author mjg
 * @since 2014-11-28
 */
public interface GroupPsnEditService {

  /**
   * 保存群组记录.
   * 
   * @param groupPsn
   */
  void saveGroupPsn(GroupPsn groupPsn);

  /**
   * 保存群组基本信息.
   */
  void saveBaseInfo(GroupBaseInfo groupBaseInfo);

  /**
   * 保存群组基本信息(包含group_filter表信息).
   * 
   * @param groupPsn
   */
  void saveGroupBaseInfo(GroupPsn groupPsn);

  /**
   * 保存群组控制信息.
   */
  void saveControl(GroupControl groupControl);

  /**
   * 保存群组控制信息.
   * 
   * @param groupPsn
   */
  void saveGroupControl(GroupPsn groupPsn);

  /**
   * 保存群组基本检索过滤条件信息.
   */
  void saveFilter(GroupFilter groupFilter);

  /**
   * 保存群组基本检索条件过滤信息.
   * 
   * @param groupPsn
   */
  void saveGroupFilter(GroupPsn groupPsn);

  /**
   * 保存群组学科关键词信息.
   */
  void saveKeyDisc(GroupKeyDisc groupKeyDisc);

  /**
   * 保存群组学科关键词信息.
   * 
   * @param groupPsn
   */
  void saveGroupKeyDisc(GroupPsn groupPsn);

  /**
   * 保存群组统计信息.
   */
  void saveStatistics(GroupStatistics groupStat);

  /**
   * 保存群组统计信息.
   * 
   * @param groupPsn
   */
  void saveGroupStatistics(GroupPsn groupPsn);

  /**
   * 更新群组简介.
   * 
   * @param sourceGroupId 源群组ID
   * @param targetGroupId 目标群组ID.
   */
  void updateGroupDesc(Long sourceGroupId, Long targetGroupId);

  /**
   * 更新群组所属人.
   * 
   * @param savePsnId
   * @param groupId
   */
  void updateGroupOwner(Long savePsnId, Long groupId);

  /**
   * 更改群组主页可见权限.
   * 
   * @param groupId
   * @param pageOpenStatus
   */
  void updateGroupPageOpen(Long groupId, int pageOpenStatus);

}
