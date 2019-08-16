package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.service.psn.register.PersonRegisterService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 人员注册 后台数据处理
 * 
 * @author tsz
 *
 */
public class PersonRegisterTaskletImpl extends BaseTasklet {


  @Autowired
  private PersonRegisterService personRegisterService;
  @Resource(name = "batchJobsNormalFactory")
  private BatchJobsFactory batchJobsNormalFactory;
  @Autowired
  private BatchJobsService batchJobsService;


  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    try {
      // 调用人员注册 后台数据处理 具体业务
      PersonRegister personRegister = JacksonUtils.jsonObject(withData, PersonRegister.class);
      personRegisterService.saveR(personRegister);
      personRegisterService.initPsnFundRecommend(personRegister.getPersonId());// 初始化人员推荐基金条件
      /*
       * // 新注册用户PDWH匹配成果任务 BatchJobs pdwhPubMatchJob =
       * batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.REGISTER_PDWH_PUB_MATCH,
       * personRegister.getPersonId(), BatchWeightEnum.A.toString());
       * batchJobsService.saveJob(pdwhPubMatchJob);
       */
      // 刷新信息完整度任务 && 创建个人主页任务
      BatchJobs psnInfoCompleteRefresh = batchJobsNormalFactory.getBatchJob(
          BatchOpenCodeEnum.REGISTER_COMPLETENESS_REFRESH, personRegister.getPersonId(), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(psnInfoCompleteRefresh);
      /*
       * // 刷新人员psnHtml任务 BatchJobs psnHtmlRefresh =
       * batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.REGISTER_PSN_HTML,
       * personRegister.getPersonId(), BatchWeightEnum.A.toString());
       * batchJobsService.saveJob(psnHtmlRefresh);
       */
    } catch (Exception e) {
      super.logger.error("人员注册初始化数据异常" + withData, e);
      throw new BatchTaskException("人员注册初始化数据异常" + withData, e);
    }
  }

}
