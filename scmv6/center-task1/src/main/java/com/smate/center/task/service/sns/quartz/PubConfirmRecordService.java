package com.smate.center.task.service.sns.quartz;

import com.smate.center.task.exception.ServiceException;

public interface PubConfirmRecordService {

  Long savePubConfirmRecord(Long psnId, Long insId, Long insPubId, Long confirmPubId);

  public void setSyncRcmdStatus(Long id, Integer syncRcmd) throws ServiceException;

}
