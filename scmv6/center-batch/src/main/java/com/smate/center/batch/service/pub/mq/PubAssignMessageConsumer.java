package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.PubRolAssignService;

/**
 * 指派消息.
 * 
 */
public class PubAssignMessageConsumer implements ILocalQueneConsumer {

  private static Logger logger = LoggerFactory.getLogger(PubAssignMessageConsumer.class);

  @Autowired
  private PubRolAssignService pubRolAssignDataService;

  public void receiveMessage(PubAssignMessage message) throws ServiceException {
    Assert.notNull(message);

    try {
      logger.debug("接受到一个成果指派请求，{}", message);
      this.pubRolAssignDataService.doAssignPub(message);
      logger.debug("完成一个成果指派请求，{}", message);
    } catch (ServiceException e) {
      throw e;
    }
  }

  @Override
  public void receive(BaseLocalQueneMessage message) throws ServiceException {
    this.receiveMessage((PubAssignMessage) message);
  }
}
