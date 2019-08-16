package com.smate.center.task.rcmd.jms;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PendingCfmPubNumMessage;

/**
 * 待认领成果数消息.
 * 
 * @author zjh
 * 
 */
@Component("pendingCfmPubNumMessageProducer")
public class PendingCfmPubNumMessageProducer {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PendingCfmPubNumMessageConsumer pendingCfmPubNumMessageConsumer;

  /**
   * 
   * @param message
   * @return
   */
  public Integer syncPendingCfmPubNum(Long psnId, Integer pcfPubNum) {
    try {
      PendingCfmPubNumMessage msg = new PendingCfmPubNumMessage(psnId, pcfPubNum, 0);
      pendingCfmPubNumMessageConsumer.receive(msg);
    } catch (ServiceException e) {
      logger.error("成果确认成果同步回单位失败" + e);
      return -1;
    }
    return 1;
  }

}
