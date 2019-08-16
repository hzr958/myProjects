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
 * a、同步群组信息(在保存群组编辑信息的时候也会用到)
 * 
 * @author zzx
 *
 */
public class SyncGroupPsnToSnsTasklet extends BaseTasklet {
  @Autowired
  private SyncGroupService syncGroupService;
  @Autowired
  GroupPsnSearchService groupPsnSearchService;

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
    Integer nodeId = Integer.parseInt(String.valueOf(jobContentMap.get("node_id")));
    Long groupId = NumberUtils.toLong(JacksonUtils.jsonObject(withData, String.class));
    GroupPsn groupPsn = groupPsnSearchService.getBuildGroupPsn(groupId);
    try {
      syncGroupService.syncGroupPsnToSns(nodeId, groupPsn);
    } catch (Exception e) {
      logger.error("a、同步群组信息出错，GroupPsn：" + groupPsn);
      throw new BatchTaskException(e);
    }
  }

}
