package com.smate.center.batch.tasklet.pub;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.pub.SavePdwhPubDataFromCrossrefService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.string.StringUtils;

public class SavePdwhPubFromCrossrefTasklet extends BaseTasklet {
  @Autowired
  private SavePdwhPubDataFromCrossrefService savePdwhPubDataFromCrossrefService;

  @Override
  public DataVerificationStatus dataVerification(String msgId) throws BatchTaskException {
    if (StringUtils.isBlank(msgId) || NumberUtils.toLong(msgId) <= 0) {
      return DataVerificationStatus.FALSE;
    }
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long originalId = NumberUtils.toLong(jobContentMap.get("msg_id").toString());
    savePdwhPubDataFromCrossrefService.handleOriginalPubData(originalId);
  }

}
