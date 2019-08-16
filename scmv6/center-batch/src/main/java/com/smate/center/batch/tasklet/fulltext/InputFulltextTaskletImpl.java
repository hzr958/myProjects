package com.smate.center.batch.tasklet.fulltext;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pub.fulltext.InputIsiPubFulltextService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 全文导入 任务
 * 
 * @author tsz
 *
 */
public class InputFulltextTaskletImpl extends BaseTasklet {

  @Autowired
  private InputIsiPubFulltextService inputIsiPubFulltextService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }


  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    inputIsiPubFulltextService.dealFile(withData);
  }

}
