package com.smate.center.batch.tasklet.confirm.pubft;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.service.confirm.pubft.PsnStatisticsMessageService;
import com.smate.center.batch.service.pub.mq.PsnStatisticsMessage;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

public class PsnStatisticsMessageTasklet extends BaseTasklet {

  @Resource(name = "batchJobsNormalFactory")
  private BatchJobsFactory batchJobsNormalFactory;
  @Autowired
  private PsnStatisticsMessageService psnStatisticsMessageService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    logger.info("------------------------------更新人员统计数据开始-----------------------------");
    String psnIdStr = String.valueOf(jobContentMap.get("msg_id"));
    try {
      if (StringUtils.isNotBlank(psnIdStr)) {
        Long psnId = NumberUtils.toLong(psnIdStr);
        if (psnId > 0) {
          String objJsonMap = JacksonUtils.mapToJsonStr((Map) jobContentMap.get("psnStatisticsMessage"));
          PsnStatisticsMessage msg = JacksonUtils.jsonObject(objJsonMap, PsnStatisticsMessage.class);
          PsnStatistics statistics = new PsnStatistics();

          statistics.setCitedSum(msg.getCitedSum());
          statistics.setEnSum(msg.getEnSum());
          statistics.setHindex(msg.getHindex());
          statistics.setPrjSum(msg.getPrjSum());
          statistics.setPubSum(msg.getPubSum());
          statistics.setZhSum(msg.getZhSum());
          statistics.setPsnId(msg.getPsnId());
          statistics.setFrdSum(msg.getFrdSum());
          statistics.setGroupSum(msg.getGroupSum());
          statistics.setPubAwardSum(msg.getPubAwardSum());
          statistics.setPatentSum(msg.getPatentSum());

          psnStatisticsMessageService.updatePsnStatics(statistics);
        }
      }
    } catch (Exception e) {
      super.logger.error("更新人员统计数据异常" + psnIdStr, e);
      throw new BatchTaskException("更新人员统计数据异常" + psnIdStr, e);
    }
  }

}
