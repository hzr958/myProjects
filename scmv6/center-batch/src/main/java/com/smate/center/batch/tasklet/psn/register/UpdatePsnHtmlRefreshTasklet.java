package com.smate.center.batch.tasklet.psn.register;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.psn.PsnHtmlService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 人员注册时，更新psnHtml列表需要处理的数据
 * 
 * 
 */
public class UpdatePsnHtmlRefreshTasklet extends BaseTasklet {

  @Autowired
  private PsnHtmlService snsPsnHtmlService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {

    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {

    Long psnId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    this.snsPsnHtmlService.updatePsnHtmlRefresh(psnId);

  }



}
