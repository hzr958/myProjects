package com.smate.center.open.service.group.psn;

import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.center.open.model.group.GroupControl;
import com.smate.center.open.model.group.GroupFilter;
import com.smate.center.open.model.group.GroupKeyDisc;
import com.smate.center.open.model.group.GroupStatistics;

/**
 * 群组检索服务
 * 
 * @author lhd
 *
 */
public interface GroupPsnSearchService {

  /**
   * 获取群组基本信息记录.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  GroupBaseInfo getBaseInfo(Long groupId);

  /**
   * 获取群组统计记录
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  GroupStatistics getGroupStatisticsByGroupId(Long groupId);

  /**
   * 获取群组检索过滤表(封装常用查询条件,检索时过滤群组)
   * 
   * @param groupId
   * @return
   */
  GroupFilter getGroupFilterByGroupId(Long groupId);

  /**
   * 获取群组控制开关表.
   * 
   * @param groupId
   * @return
   */
  GroupControl getGroupControlByGroupId(Long groupId);

  /**
   * 获取群组学科关键词表.
   * 
   * @param groupId
   * @return
   */
  GroupKeyDisc getGroupKeyDiscByGroupId(Long groupId);
}
