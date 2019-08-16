package com.smate.center.task.rcmd.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PendingCfmPubNumMessage;
import com.smate.center.task.single.service.person.PsnStatisticsUpdateService;

@Component("pendingCfmPubNumMessageConsumer")
public class PendingCfmPubNumMessageConsumer {
  /**
   * 
   */
  private static final long serialVersionUID = 1919464598909016618L;
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;

  public void receive(PendingCfmPubNumMessage message) throws ServiceException {
    Assert.notNull(message);
    PendingCfmPubNumMessage msg = (PendingCfmPubNumMessage) message;
    psnStatisticsUpdateService.setPsnPendingConfirmPubNum(msg.getPsnId(), msg.getPcfPubNum());
  }

}
