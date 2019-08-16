package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.PubRolDupHandlerService;


/**
 * 成果查重.
 * 
 */
public class PubDupCheckMessageConsumer implements ILocalQueneConsumer {

  private static Logger logger = LoggerFactory.getLogger(PubDupCheckMessageConsumer.class);

  @Autowired
  private PubRolDupHandlerService pubRolDupHandlerService;

  public void receiveMessage(PubDupCheckMessage event) throws ServiceException {

    Assert.notNull(event);

    try {

      logger.debug("接受到一个成果查重请求，{},{}", event.getPubId(), event.getInsId());
      this.pubRolDupHandlerService.check(event.getInsId(), event.getPubId());
      logger.debug("完成一个成果查重请求，{},{}", event.getPubId(), event.getInsId());
      // 删除临时库成果操作
      this.pubRolDupHandlerService.deleteDupTempPub(event.getInsId(), event.getPubId());
    } catch (ServiceException e) {
      throw e;
    }
  }

  @Override
  public void receive(BaseLocalQueneMessage message) throws ServiceException {
    this.receiveMessage((PubDupCheckMessage) message);
  }
}
