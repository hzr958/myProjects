package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 待认领成果数消息.
 * 
 * @author lqh
 * 
 */
@Component("pendingCfmPubNumMessageProducer")
public class PendingCfmPubNumMessageProducer {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String queueName = "PendingCfmPubNumMessage";
  @Autowired
  private PendingCfmPubNumMessageConsumer pendingCfmPubNumMessageConsumer;


  /**
   * 
   * @param message
   * @return
   */
  public Integer syncPendingCfmPubNum(Long psnId, Integer pcfPubNum) {
    try {
      PendingCfmPubNumMessage msg = new PendingCfmPubNumMessage(psnId, pcfPubNum);
      this.pendingCfmPubNumMessageConsumer.receive(msg);
    } catch (ServiceException e) {
      logger.error("成果确认成果同步回单位失败" + e);
      return -1;
    }
    return 1;
  }

}
