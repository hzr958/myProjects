package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.service.psn.register.PersonRegisterService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 新注册用户PDWH匹配成果
 * 
 */
public class MatchPdwhPubTasklet extends BaseTasklet {

  @Autowired
  private PersonRegisterService personRegisterService;

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
    String psnIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long psnId = Long.parseLong(psnIdStr);
    PersonRegister person = this.personRegisterService.getPersonRegisterInfo(psnId);
    this.personRegisterService.matchPdwhPub(person);
  }



}
