package com.smate.center.batch.tasklet.group;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.service.group.SyncGroupService;
import com.smate.center.batch.service.pub.GroupPsnSearchService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * b、同步群组成员（传递GroupPsn对象和invite_psn_id）
 * 
 * @author zzx
 *
 */
public class SyncGroupInvitePsnToSnsTasklet extends BaseTasklet {
  @Autowired
  private SyncGroupService syncGroupService;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long groupId = NumberUtils.toLong(JacksonUtils.jsonObject(withData, String.class));
    if (groupId == null || groupId == 0)
      return DataVerificationStatus.NULL;
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    Long invitePsnId = Long.parseLong(String.valueOf(jobContentMap.get("invite_psn_id")));
    Long groupId = NumberUtils.toLong(JacksonUtils.jsonObject(withData, String.class));
    GroupPsn groupPsn = groupPsnSearchService.getBuildGroupPsn(groupId);
    try {
      if (groupPsn != null) {
        syncGroupService.syncGroupInvitePsnToSns(invitePsnId, groupPsn);
      }

    } catch (Exception e) {
      logger.error("b、同步群组成员（传递GroupPsn对象和invite_psn_id）出错，GroupPsn：" + groupPsn + "invitePsnId:" + invitePsnId);
      throw new BatchTaskException(e);
    }

  }

}
