package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubXmlSyncEvent;
import com.smate.center.batch.service.pdwh.pub.RolPubXmlSyncService;

/**
 * 从SNS拉提交成果的XML.
 * 
 * @author yamingd
 * 
 */
public class PullXmlSyncMessageConsumer implements ILocalQueneConsumer {

  private static Logger logger = LoggerFactory.getLogger(PullXmlSyncMessageConsumer.class);

  @Autowired
  private RolPubXmlSyncService rolPubXmlSyncService;

  public void receiveMessage(PubXmlSyncEvent event) throws ServiceException {

    try {
      logger.debug("接受到一个Pull Xml From SNS请求，{}", event);
      this.rolPubXmlSyncService.pullFromSNS(event);
      logger.debug("完成一个Pull Xml From SNS请求，{}", event);

    } catch (ServiceException e) {
      throw e;
    }
  }

  @Override
  public void receive(BaseLocalQueneMessage message) throws ServiceException {
    this.receiveMessage((PubXmlSyncEvent) message);
  }
}
