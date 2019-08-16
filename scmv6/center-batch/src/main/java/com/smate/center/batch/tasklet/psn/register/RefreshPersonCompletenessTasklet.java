package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnProfileUrlService;
import com.smate.center.batch.service.psn.register.PersonRegisterService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 更新注册人员信息完整度，查找或创建个人主页url
 * 
 * 
 */
public class RefreshPersonCompletenessTasklet extends BaseTasklet {

  @Autowired
  private PersonRegisterService personRegisterService;
  @Autowired
  private PersonManager personManager;


  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long psnId = Long.parseLong(withData);
    PersonRegister person = this.personRegisterService.getPersonRegisterInfo(psnId);

    if (person == null)
      return DataVerificationStatus.NULL;

    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    this.personManager.refreshComplete(psnId);

  }



}
