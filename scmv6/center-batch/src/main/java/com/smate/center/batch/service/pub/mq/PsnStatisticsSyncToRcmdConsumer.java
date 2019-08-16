package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;
import com.smate.center.batch.service.pub.rcmd.RcmdPsnStatisticsSyncService;

/**
 * 同步PsnStatistics记录到推荐服务（接收端） rcmd
 * 
 * @author zk
 *
 */
@Component("psnStatisticsSyncToRcmdConsumer")
public class PsnStatisticsSyncToRcmdConsumer {

  /**
   * 
   */
  private static Logger logger = LoggerFactory.getLogger(PsnStatisticsSyncToRcmdConsumer.class);
  @Autowired
  private RcmdPsnStatisticsSyncService syncService;

  public void receive(PsnStatisticsMessage message) throws ServiceException {
    try {
      PsnStatisticsMessage msg = (PsnStatisticsMessage) message;
      RcmdPsnStatistics statistics = new RcmdPsnStatistics();
      statistics.setCitedSum(msg.getCitedSum());
      statistics.setEnSum(msg.getEnSum());
      statistics.setHindex(msg.getHindex());
      statistics.setPrjSum(msg.getPrjSum());
      statistics.setPubSum(msg.getPubSum());
      statistics.setZhSum(msg.getZhSum());
      statistics.setPsnId(msg.getPsnId());
      statistics.setFrdSum(msg.getFrdSum());
      statistics.setGroupSum(msg.getGroupSum());
      statistics.setPubAwardSum(msg.getPubAwardSum());
      statistics.setPatentSum(msg.getPatentSum());
      statistics.setPcfPubSum(msg.getPcfPubSum());
      statistics.setPubFullTextSum(msg.getPubFullTextSum());
      syncService.save(statistics);
    } catch (Exception e) {
      logger.error("发送PsnStatistics记录到推荐服务变更同步MQ消息失败", e);
      throw new ServiceException(e);
    }

  }

}
