package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;

@Component("pubConfirmXmlSyncProducer")
public class PubConfirmXmlSyncProducer {

  /**
   * 
   */
  private static final long serialVersionUID = -8561876138052638010L;
  private static Logger logger = LoggerFactory.getLogger(PubConfirmXmlSyncProducer.class);
  private String queueName = "pubConfirmSnsXmlSync";
  @Autowired
  private PubConfirmXmlSyncMessageConsumer pubConfirmXmlSyncMessageConsumer;

  public String getQueueName() {
    return this.queueName;
  }


  public void pubConfirmSyncMessage(PubConfirmSyncMessage message) throws ServiceException {
    if (message.getInsId() == null || message.getPsnId() == null) {
      return;
    }
    pubConfirmXmlSyncMessageConsumer.receive(message);

  }

}
