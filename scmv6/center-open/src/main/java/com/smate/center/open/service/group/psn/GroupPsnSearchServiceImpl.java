package com.smate.center.open.service.group.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupBaseinfoDao;
import com.smate.center.open.dao.group.GroupControlDao;
import com.smate.center.open.dao.group.GroupFilterDao;
import com.smate.center.open.dao.group.GroupKeyDiscDao;
import com.smate.center.open.dao.group.GroupStatisticsDao;
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
@Service("groupPsnSearchService")
@Transactional(rollbackFor = Exception.class)
public class GroupPsnSearchServiceImpl implements GroupPsnSearchService {
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupControlDao groupControlDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;

  /**
   * 获取群组基本信息记录.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupBaseInfo getBaseInfo(Long groupId) {
    return groupBaseInfoDao.getGroupBaseInfo(groupId);
  }

  /**
   * 获取群组统计记录
   * 
   * @author lhd
   */
  @Override
  public GroupStatistics getGroupStatisticsByGroupId(Long groupId) {
    return groupStatisticsDao.getStatistics(groupId);
  }

  /**
   * 获取群组检索过滤表(封装常用查询条件,检索时过滤群组)
   * 
   * @author zzx
   */
  @Override
  public GroupFilter getGroupFilterByGroupId(Long groupId) {
    return groupFilterDao.getGroupFilter(groupId);
  }

  /**
   * 获取群组控制开关表.
   * 
   * @author zzx
   */
  @Override
  public GroupControl getGroupControlByGroupId(Long groupId) {
    return groupControlDao.getGroupControl(groupId);
  }

  /**
   * 获取群组学科关键词表.
   * 
   * @author zzx
   */
  @Override
  public GroupKeyDisc getGroupKeyDiscByGroupId(Long groupId) {
    return groupKeyDiscDao.getGroupKeyDisc(groupId);
  }
}
