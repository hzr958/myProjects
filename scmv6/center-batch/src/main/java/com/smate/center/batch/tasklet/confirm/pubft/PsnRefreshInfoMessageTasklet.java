package com.smate.center.batch.tasklet.confirm.pubft;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.service.confirm.pubft.PsnRefreshUserInfoDBService;
import com.smate.center.batch.service.pub.mq.PsnRefreshInfoMessage;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

public class PsnRefreshInfoMessageTasklet extends BaseTasklet {

  @Resource(name = "batchJobsNormalFactory")
  private BatchJobsFactory batchJobsNormalFactory;
  @Autowired
  private PsnRefreshUserInfoDBService psnRefreshUserInfoDBService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    logger.info("------------------------------更新人员统计数据开始-----------------------------");

    String objMapStr = JacksonUtils.mapToJsonStr((Map) jobContentMap.get("message"));
    PsnRefreshInfoMessage msg = JacksonUtils.jsonObject(objMapStr, PsnRefreshInfoMessage.class);
    try {

      logger.debug("接收到了人员冗余信息刷新消息：psnId=" + msg.getPsnId());
      psnRefreshUserInfoDBService.saveRefreshInfo(msg);
    } catch (Exception e) {
      logger.error("接收到了人员冗余信息刷新消息保存错误：psnId=" + msg.getPsnId(), e);
    }
  }

}
