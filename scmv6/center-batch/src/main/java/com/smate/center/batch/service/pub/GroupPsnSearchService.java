package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.model.sns.pub.GroupBaseInfo;
import com.smate.center.batch.model.sns.pub.GroupControl;
import com.smate.center.batch.model.sns.pub.GroupFilter;
import com.smate.center.batch.model.sns.pub.GroupKeyDisc;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupStatistics;

/**
 * 群组检索服务(原group_psn表拆分为多个表，为减少对其他模块的影响，增加此服务以便数据封装)_SCM-6000.
 * 
 * @author mjg
 * @since 2014-11-28
 */
public interface GroupPsnSearchService {

  /**
   * 获取群组统计记录.
   * 
   * @param groupId
   * @return
   */
  GroupStatistics getStatistics(Long groupId);

  /**
   * 获取群组基本信息记录.
   * 
   * @param groupId
   * @return
   */
  GroupBaseInfo getBaseInfo(Long groupId);

  /**
   * 获取群组检索过滤信息.
   * 
   * @param groupId
   * @return
   */
  GroupFilter getFileter(Long groupId);

  /**
   * 获取群组控制记录.
   * 
   * @param groupId
   * @return
   */
  GroupControl getControl(Long groupId);

  /**
   * 获取群组学科关键词记录.
   * 
   * @param groupId
   * @return
   */
  GroupKeyDisc getKeyDisc(Long groupId);

  /**
   * 获取群组基本信息(封装为原群组主表对象以供检索调用)<因GroupFilter信息较多地方使用因此同步封装GroupFilter信息>.
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupBaseInfo(Long groupId);

  /**
   * 获取群组控制信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupControl(Long groupId);

  /**
   * 获取群组学科关键词信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupKeyDisc(Long groupId);

  /**
   * 获取群组统计信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupStatistics(Long groupId);

  /**
   * 获取群组过滤检索信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupFilter(Long groupId);

  /**
   * 获取群组信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getGroupPsn(Long groupId);

  /**
   * 获取群组信息--用于群组推荐在发现群组显示.
   * 
   * @param groupId
   * @return
   */
  Map<String, Object> getGroupPsnMap(Long groupId, Long psnId);

  /**
   * 获取迁移的任务记录列表.
   * 
   * @param startGroupId
   * @param maxSize
   * @return
   */
  List<Long> getTaskIdList(Long startGroupId, int maxSize);

  /**
   * 获取群组记录.
   */
  GroupPsn getGroupPsnRecord(Long groupId);

  /**
   * 获取group_baseInfo表最大群组ID.
   * 
   * @return
   */
  Long getMaxBaseInfoGroupId();

  /**
   * 根据groupId获取GroupPsn对象
   * 
   * @param groupId
   * @return
   */
  GroupPsn getBuildGroupPsn(Long groupId);
}
