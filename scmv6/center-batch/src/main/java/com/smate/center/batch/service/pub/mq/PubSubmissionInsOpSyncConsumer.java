package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.pubsubmission.PubSubmissionService;

/**
 * 接收单位的成果批准结果消息同步消费者.
 * 
 * @author LY
 * 
 */
@Component("pubSubmissionInsOpSyncConsumer")
public class PubSubmissionInsOpSyncConsumer {

  private static Logger logger = LoggerFactory.getLogger(PubSubmissionInsOpSyncConsumer.class);
  @Autowired
  private PubSubmissionService pubSubmmissionService;

  public void receive(PubSubmissionInsSyncMessage consumerMessage) throws ServiceException {

    if (consumerMessage == null || consumerMessage.getPsnId() == null || consumerMessage.getInsId() == null
        || consumerMessage.getPubId() == null) {
      throw new ServiceException("");
    }
    try {
      pubSubmmissionService.receiverPubSubmissionSyncMessage(consumerMessage);
    } catch (ServiceException e) {
      logger.error("接收单位的成果批准结果失败" + e.getMessage());
    }
  }

}
