package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 成果查重消息.
 * 
 * @author yamingd
 * 
 */
@Component
public class PubDupCheckMessageProducer extends AbstractLocalQueneMessageProducer {

  /**
   * 
   */
  private static final long serialVersionUID = -7781195831886089830L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String cBeanName = "pubDupCheckMessageConsumer";

  /**
   * 发出成果查重消息.
   * 
   * @param insPubId
   * @param insId
   */
  public void sendDupCheckMessage(PubDupCheckMessage message) throws Exception {
    Assert.notNull(message);

    // 灾难处理，需要保存消息到数据库.
    this.sendMessage(message);
  }

  @Override
  public String getCbeanName() {
    return cBeanName;
  }
}
