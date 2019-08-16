package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 同步PsnStatistics
 * 
 * @author zk
 * 
 */
@Component("psnStatisticsSyncProducer")
public class PsnStatisticsSyncProducer {

  @Autowired
  private PsnStatisticsSyncToRcmdConsumer psnStatisticsSyncToRcmdConsumer;
  @Autowired
  private PsnStatisticsSyncToRolConsumer psnStatisticsSyncToRolConsumer;

  public void syncMessage(PsnStatistics statistics) throws ServiceException {
    PsnStatisticsMessage message = new PsnStatisticsMessage();
    message.setCitedSum(statistics.getCitedSum());
    message.setEnSum(statistics.getEnSum());
    message.setHindex(statistics.getHindex());
    message.setPrjSum(statistics.getPrjSum());
    message.setPubSum(statistics.getPubSum());
    message.setZhSum(statistics.getZhSum());
    message.setPsnId(statistics.getPsnId());
    message.setFrdSum(statistics.getFrdSum());
    message.setGroupSum(statistics.getGroupSum());
    message.setPubAwardSum(statistics.getPubAwardSum());
    message.setPatentSum(statistics.getPatentSum());
    message.setPcfPubSum(statistics.getPcfPubSum());
    message.setPubFullTextSum(statistics.getPubFullTextSum());
    psnStatisticsSyncToRcmdConsumer.receive(message);
    psnStatisticsSyncToRolConsumer.receive(message);
  }

}
