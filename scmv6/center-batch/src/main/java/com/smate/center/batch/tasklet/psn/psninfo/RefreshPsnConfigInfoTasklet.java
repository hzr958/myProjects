package com.smate.center.batch.tasklet.psn.psninfo;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.service.psn.psncnf.PsnCnfReBuildService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

public class RefreshPsnConfigInfoTasklet extends BaseTasklet {

  @Resource(name = "batchJobsNormalFactory")
  private BatchJobsFactory batchJobsNormalFactory;
  @Autowired
  private PsnCnfReBuildService psnCnfReBuildService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    logger.info("------------------------------更新人员psn_config_XXX数据开始-----------------------------");
    String psnIdStr = String.valueOf(jobContentMap.get("msg_id"));
    try {
      if (StringUtils.isNotBlank(psnIdStr)) {
        Long psnId = NumberUtils.toLong(psnIdStr);
        if (psnId > 0) {
          psnCnfReBuildService.init(psnId);
        }
      }
    } catch (Exception e) {
      super.logger.error("更新人员psn_config_XXX数据异常" + psnIdStr, e);
      throw new BatchTaskException("更新人员psn_config_XXX数据异常" + psnIdStr, e);
    }
  }

}
