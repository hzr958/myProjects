package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.model.rol.pub.PubXmlSyncEvent;

/**
 * 从SNS拉XML.
 * 
 */
@Component
public class PullXmlSyncMessageProducer extends AbstractLocalQueneMessageProducer {

  /**
   * 
   */
  private static final long serialVersionUID = -4597882873633990842L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String cBeanName = "pullXmlSyncMessageConsumer";

  /**
   * 从个人库拉数据.
   * 
   * @param event
   */
  public void sendPullXmlMessage(PubXmlSyncEvent message) throws Exception {
    Assert.notNull(message);
    this.sendMessage(message);
  }

  @Override
  public String getCbeanName() {
    return cBeanName;
  }
}
