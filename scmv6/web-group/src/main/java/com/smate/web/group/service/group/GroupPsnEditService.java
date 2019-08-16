package com.smate.web.group.service.group;

import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.model.group.GroupControl;
import com.smate.web.group.model.group.GroupFilter;
import com.smate.web.group.model.group.GroupKeyDisc;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.GroupStatistics;

/**
 * 群组编辑保存
 * 
 * @author zjh
 *
 */
public interface GroupPsnEditService {
  /**
   * 保存群组记录.
   * 
   * @param groupPsn
   */
  void saveGroupPsn(GroupPsn groupPsn) throws Exception;

  /**
   * 保存群组基本信息
   * 
   * @param groupBaseInfo
   */
  void saveBaseInfo(GroupBaseInfo groupBaseInfo);

  /**
   * 保存群组控制信息.
   */
  void saveControl(GroupControl groupControl);

  void saveFilter(GroupFilter groupFilter);

  void saveKeyDisc(GroupKeyDisc groupKeyDisc);

  void saveStatistics(GroupStatistics groupStat);

}
