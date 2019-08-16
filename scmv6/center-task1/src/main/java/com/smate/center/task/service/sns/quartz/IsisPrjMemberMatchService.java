package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.sns.quartz.GroupFundInfoMembers;

/**
 * IsisPrjMemberMatchTask 任务接口
 * 
 * @author LJ
 *
 */

public interface IsisPrjMemberMatchService {

  public List<GroupFundInfoMembers> getGroupFundInfoMembers(Integer size, Long startGroupId, Long endGroupId);


  public void handleGroupMemberInfo(GroupFundInfoMembers groupFundInfoMembers) throws Exception;


  public void saveOpResult(GroupFundInfoMembers groupFundInfoMembers, int i);



}
