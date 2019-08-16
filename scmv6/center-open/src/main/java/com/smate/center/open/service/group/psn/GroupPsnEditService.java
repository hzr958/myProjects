package com.smate.center.open.service.group.psn;

import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.center.open.model.group.GroupControl;
import com.smate.center.open.model.group.GroupFilter;
import com.smate.center.open.model.group.GroupKeyDisc;
import com.smate.center.open.model.group.GroupPsn;
import com.smate.center.open.model.group.GroupStatistics;


/**
 * 群组编辑服务
 * 
 * @author lhd
 *
 */
public interface GroupPsnEditService {

  /**
   * 保存群组记录.
   * 
   * @author lhd
   * @param groupPsn
   */
  void saveGroupPsn(GroupPsn groupPsn, Long openId) throws Exception;

  /**
   * 保存群组基本信息.
   * 
   * @author lhd
   */
  void saveBaseInfo(GroupBaseInfo groupBaseInfo) throws Exception;

  /**
   * 保存群组控制信息.
   * 
   * @author lhd
   */
  void saveControl(GroupControl groupControl) throws Exception;

  /**
   * 保存群组基本检索过滤条件信息.
   * 
   * @author lhd
   */
  void saveFilter(GroupFilter groupFilter) throws Exception;

  /**
   * 保存群组学科关键词信息.
   * 
   * @author lhd
   */
  void saveKeyDisc(GroupKeyDisc groupKeyDisc) throws Exception;

  /**
   * 保存群组统计信息.
   * 
   * @author lhd
   */
  void saveStatistics(GroupStatistics groupStat) throws Exception;
}
