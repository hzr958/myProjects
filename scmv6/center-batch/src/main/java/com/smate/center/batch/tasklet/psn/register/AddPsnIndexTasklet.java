package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.user.UserService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * SCM-11119科研之友刚注册的人员不能在检索人员中找到，原因是没有跑家索引的任务
 * 
 * 
 */
public class AddPsnIndexTasklet extends BaseTasklet {

  @Autowired
  private UserService userService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    if (jobContentMap.get("msg_id") != null) {
      Long psnId = NumberUtils.toLong(jobContentMap.get("msg_id").toString());
      userService.updateSolrPsnInfo(psnId);
    }


  }

}
