package com.smate.center.batch.tasklet.confirm.pubft;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.service.pub.RcmdSyncDBService;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessage;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

public class RcmdSyncFlagMessageTasklet extends BaseTasklet {

  @Resource(name = "batchJobsNormalFactory")
  private BatchJobsFactory batchJobsNormalFactory;

  @Autowired
  private RcmdSyncDBService rcmdSyncDBService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    logger.info("------------------------------人员冗余信息同步标记消息数据开始-----------------------------");
    String objMapStr = JacksonUtils.mapToJsonStr((Map) jobContentMap.get("message"));
    RcmdSyncFlagMessage message = JacksonUtils.jsonObject(objMapStr, RcmdSyncFlagMessage.class);
    try {
      rcmdSyncDBService.saveSyncInfo(message);
      logger.debug("人员冗余信息同步标记消息数据已处理");
    } catch (Exception e) {
      logger.error("人员冗余信息同步标记消息数据处理:psnid=" + (message != null ? message.getPsnId() : "null"));
    }
  }

}
