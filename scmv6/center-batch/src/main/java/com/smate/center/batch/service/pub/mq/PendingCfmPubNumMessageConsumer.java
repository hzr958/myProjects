package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;

/**
 * 待认领成果数消息.
 * 
 * @author lqh
 * 
 */
@Component("pendingCfmPubNumMessageConsumer")
public class PendingCfmPubNumMessageConsumer {

  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;

  public void receive(PendingCfmPubNumMessage msg) throws ServiceException {
    Assert.notNull(msg);
    psnStatisticsUpdateService.setPsnPendingConfirmPubNum(msg.getPsnId(), msg.getPcfPubNum());
  }
}
