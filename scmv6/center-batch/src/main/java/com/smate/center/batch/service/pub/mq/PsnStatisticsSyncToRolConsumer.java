package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.SiePsnStatistics;
import com.smate.center.batch.service.rol.pub.SiePsnHtmlService;
import com.smate.center.batch.service.rol.pub.SiePsnStatisticsSyncService;

/**
 * 同步PsnStatistics记录到Rol（接收端）. sie
 * 
 * @author zyx
 *
 */
@Component("psnStatisticsSyncToRolConsumer")
public class PsnStatisticsSyncToRolConsumer {

  /**
   * 
   */
  private static Logger logger = LoggerFactory.getLogger(PsnStatisticsSyncToRolConsumer.class);
  @Autowired
  private SiePsnStatisticsSyncService syncService;
  @Autowired
  private SiePsnHtmlService psnHtmlService;

  public void receive(PsnStatisticsMessage message) throws ServiceException {
    try {
      PsnStatisticsMessage msg = (PsnStatisticsMessage) message;
      SiePsnStatistics statistics = new SiePsnStatistics();

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

      syncService.save(statistics);
      this.psnHtmlService.saveToRefreshTask(statistics.getPsnId());
    } catch (Exception e) {
      logger.error("发送PsnStatistics记录到Rol变更同步MQ消息失败", e);
      throw new ServiceException(e);
    }

  }

}
