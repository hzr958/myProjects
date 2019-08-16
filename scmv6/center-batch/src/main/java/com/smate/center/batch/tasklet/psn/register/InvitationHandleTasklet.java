package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.service.psn.register.PersonRegisterService;
import com.smate.center.batch.service.pub.mq.MessageService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 注册时处理friendInvitation || groupInvitation msg_id中是存储的person类的jason格式的数据
 */
public class InvitationHandleTasklet extends BaseTasklet {

  @Autowired
  private PersonRegisterService personRegisterService;
  @Autowired
  private MessageService messageService;
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
    // Long psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    // PersonRegister person = this.personRegisterService.getPersonRegisterInfo(psnId);
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    PersonRegister personRegister = JacksonUtils.jsonObject(withData, PersonRegister.class);
    this.messageService.genInviteAction(personRegister);

    // 新注册用户PDWH匹配成果任务
    BatchJobs rcmdSyncPsnInfoJob = batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.REGISTER_RCMD_SYN_PSN_INFO,
        personRegister.getPersonId(), BatchWeightEnum.A.toString());
    batchJobsService.saveJob(rcmdSyncPsnInfoJob);

    // 冗余用户信息至RCMD任务
    BatchJobs pdwhPubMatchJob = batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.REGISTER_PDWH_PUB_MATCH,
        personRegister.getPersonId(), BatchWeightEnum.A.toString());
    batchJobsService.saveJob(pdwhPubMatchJob);
    // 刷新信息完整度任务 && 创建个人主页任务
    BatchJobs psnInfoCompleteRefresh = batchJobsNormalFactory.getBatchJob(
        BatchOpenCodeEnum.REGISTER_COMPLETENESS_REFRESH, personRegister.getPersonId(), BatchWeightEnum.A.toString());
    batchJobsService.saveJob(psnInfoCompleteRefresh);
    // 刷新人员psnHtml任务
    BatchJobs psnHtmlRefresh = batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.REGISTER_PSN_HTML,
        personRegister.getPersonId(), BatchWeightEnum.A.toString());
    batchJobsService.saveJob(psnHtmlRefresh);

  }

}
