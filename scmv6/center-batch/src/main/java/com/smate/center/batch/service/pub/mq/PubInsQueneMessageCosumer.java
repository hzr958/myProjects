package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.PubInsSyncRolService;


/**
 * PSN-INS topic消息消费.
 * 
 */
public class PubInsQueneMessageCosumer {

  @Autowired
  private PubInsSyncRolService pubInsSyncRolService;

  public void receive(PubInsSyncMessage message) throws ServiceException {

    PubInsSyncActionEnum action = message.getAction();
    if (action == PubInsSyncActionEnum.Add) {
      this.pubInsSyncRolService.savePubIns(message);
    } else if (action == PubInsSyncActionEnum.Delete) {
      this.pubInsSyncRolService.deletePubIns(message);
    }
  }
}
