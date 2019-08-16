package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;
import com.smate.center.task.service.sns.quartz.PubConfirmSyncService;

/**
 * 成果指派MQ消息，包括成果XML.
 * 
 * @author liqinghua
 * 
 */
@Component("pubConfirmSyncMessageConsumer")
public class PubConfirmSyncMessageConsumer {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubConfirmSyncService pubConfirmSyncService;

  public void receive(PubAssignSyncMessage message) throws ServiceException {

    try {
      PubAssignSyncMessage msg = (PubAssignSyncMessage) message;
      PubAssignSyncMessageEnum actionType = msg.getActionType();
      // 指派同步XML
      if (PubAssignSyncMessageEnum.SYNC_ASSIGN_XML.equals(actionType)) {
        Long pubId = pubConfirmSyncService.receiveSyncPub(msg);
      }
    } catch (Exception e) {
      logger.error("接受成果指派消息错误", e);
      throw new ServiceException(e);
    }

  }

}
