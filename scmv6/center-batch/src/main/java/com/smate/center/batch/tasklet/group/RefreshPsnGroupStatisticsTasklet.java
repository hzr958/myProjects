package com.smate.center.batch.tasklet.group;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * d、更新人员统计表群组数
 * 
 * @author zzx
 *
 */
public class RefreshPsnGroupStatisticsTasklet extends BaseTasklet {
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long psnId = Long.parseLong(withData);
    if (psnId == null || psnId == 0L)
      return DataVerificationStatus.NULL;
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long psnId = null;
    try {
      psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
      psnStatisticsUpdateService.refreshPsnGroupStatistics(psnId);
    } catch (Exception e) {
      logger.error("d、更新人员统计表群组数出错，psnId：" + psnId);
      throw new BatchTaskException(e);
    }
  }

}
