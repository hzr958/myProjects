package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;

public interface PubRolPersonService {

  /**
   * 个人确认成果后，将确认成果反馈给单位.
   * 
   * @param msg
   * @throws ServiceException
   */
  void receiveConfirmMessage(PubConfirmSyncMessage msg) throws ServiceException;

  void pubConfirmSuccessSyncXml(PubConfirmSyncMessage message) throws ServiceException;

}
