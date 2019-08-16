package com.smate.center.batch.tasklet.pub;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubDeleteComplementaryTasklet extends BaseTasklet {

  @Autowired
  private PublicationService publicationService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    // 不需要再验证数据
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String data = String.valueOf(jobContentMap.get("msg_id"));

    if (StringUtils.isEmpty(data)) {
      String errorMsg = "PubDeleteComplementaryTask获取数据错误，DATA：" + data;
      throw new BatchTaskException(errorMsg);
    }

    this.publicationService.deletePublicationComplementaryProcesses(data);

  }


}
