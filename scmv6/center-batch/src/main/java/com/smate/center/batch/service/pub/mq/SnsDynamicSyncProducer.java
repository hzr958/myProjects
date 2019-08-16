package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Dynamic;

@Component("snsDynamicSyncProducer")
public class SnsDynamicSyncProducer {

  @Autowired
  private SnsDynamicSyncConsumer snsDynamicSyncConsumer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public void sendSyncMessage(int fromNode, Dynamic dynamic, String extJson, int resType) throws ServiceException {
    Assert.notNull(dynamic, "dynamic不能为空.");
    logger.info("发送同步动态{}", dynamic);
    SnsDynamicSyncMessage message = new SnsDynamicSyncMessage(fromNode, dynamic, extJson, resType);
    snsDynamicSyncConsumer.receive(message);
  }

}
