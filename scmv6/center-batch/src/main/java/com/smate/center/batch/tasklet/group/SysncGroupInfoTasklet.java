package com.smate.center.batch.tasklet.group;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.group.SyncGroupService;
import com.smate.center.batch.service.pub.GroupPsnSearchService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 同步群组信息到群组成员（group_invite_psn_node表）删除GROUP_INVITE_PSNFRIEND表对应的记录， 同步信息到GROUP_PSN_NODE表
 * 
 * @author zzx
 *
 */
public class SysncGroupInfoTasklet extends BaseTasklet {
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private SyncGroupService syncGroupService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long groupId = Long.parseLong(withData);
    if (groupId == null)
      return DataVerificationStatus.NULL;
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long groupId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    try {
      syncGroupService.syncGroupInfoToGroupPsn(groupId);
    } catch (Exception e) {
      logger.error("同步群组信息到群组成员出错，groupId：" + groupId);
      throw new BatchTaskException(e);
    }



  }



}
