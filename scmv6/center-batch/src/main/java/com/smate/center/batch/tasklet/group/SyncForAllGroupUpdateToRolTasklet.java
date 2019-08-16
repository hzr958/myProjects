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
 * c、群组信息和成员有变动，同步到ROL(合作分析)(传递GroupPsn对象) 在保存群组编辑信息的时候也会用到
 * 
 * @author zzx
 *
 */
public class SyncForAllGroupUpdateToRolTasklet extends BaseTasklet {
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
    Long groupId = NumberUtils.toLong(JacksonUtils.jsonObject(withData, String.class));
    GroupPsn groupPsn = groupPsnSearchService.getBuildGroupPsn(groupId);
    try {
      if (groupPsn != null) {
        syncGroupService.syncForAllGroupUpdateToRol(groupPsn);
      }
    } catch (Exception e) {
      logger.error("c、群组信息和成员有变动，同步到ROL出错，GroupPsn：" + groupPsn);
      throw new BatchTaskException(e);
    }

  }

}
