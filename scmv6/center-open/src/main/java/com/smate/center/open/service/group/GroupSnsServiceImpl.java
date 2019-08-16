package com.smate.center.open.service.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupInvitePsnDao;
import com.smate.center.open.model.group.GroupStatistics;
import com.smate.center.open.service.group.psn.GroupPsnEditService;
import com.smate.center.open.service.group.psn.GroupPsnSearchService;

/**
 * 群组同步接口实现类
 * 
 * @author lhd
 *
 */
@Service("groupSnsService")
@Transactional(rollbackFor = Exception.class)
public class GroupSnsServiceImpl implements GroupSnsService {

  private static final long serialVersionUID = -357094199024640365L;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupPsnEditService groupPsnEditService;

  /**
   * 保存群组统计信息
   * 
   * @author lhd
   */
  @Override
  public void reCountGroupMembersByGroupId(Long groupId) throws Exception {
    GroupStatistics statistics = groupPsnSearchService.getGroupStatisticsByGroupId(groupId);
    if (statistics != null) {
      Integer sumMembers = groupInvitePsnDao.findGroupMemberCount(groupId);
      Integer sumToMembers = groupInvitePsnDao.findGroupToMemberCount(groupId);
      statistics.setSumMembers(sumMembers);
      statistics.setSumToMembers(sumToMembers);
      groupPsnEditService.saveStatistics(statistics);
    }
  }

}
