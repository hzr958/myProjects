package com.smate.center.batch.tasklet.group;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pub.RcmdSyncDBService;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessage;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * e、群组信息和成员变动，同步到推荐服务（传RcmdSyncFlag对象）在后面的保存群组编辑信息中也会用到
 * 
 * @author zzx
 *
 */
public class SaveRcmdSyncInfoTasklet extends BaseTasklet {
  @Autowired
  private RcmdSyncDBService rcmdSyncDBService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    RcmdSyncFlagMessage flag = JacksonUtils.jsonObject(withData, RcmdSyncFlagMessage.class);
    if (flag == null)
      return DataVerificationStatus.NULL;
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    RcmdSyncFlagMessage flag = JacksonUtils.jsonObject(withData, RcmdSyncFlagMessage.class);
    try {
      rcmdSyncDBService.saveRcmdSyncInfo(flag);
    } catch (Exception e) {
      logger.error("e、群组信息和成员变动，同步到推荐服务出错，RcmdSyncFlagMessage：" + flag);
      throw new BatchTaskException(e);
    }
  }

}
