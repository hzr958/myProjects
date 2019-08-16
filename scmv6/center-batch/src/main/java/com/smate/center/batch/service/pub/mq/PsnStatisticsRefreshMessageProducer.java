package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 人员统计信息更新服务.
 * 
 * @author lqh
 * 
 */
@Component
public class PsnStatisticsRefreshMessageProducer {

  @Autowired
  private PsnStatisticsRefreshMessageConsumer psnStatisticsRefreshMessageConsumer;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  private PsnStatisticsRefreshMessage initMessage(long psnId) {

    PsnStatisticsRefreshMessage message = new PsnStatisticsRefreshMessage(psnId);
    return message;
  }

  /**
   * 成果编辑、导入、删除时调用.
   * 
   * @param psnStatistics
   * @throws ServiceException
   */
  public void refreshPsnPubStatistics(Long psnId) {

    try {
      PsnStatisticsRefreshMessage msg = this.initMessage(psnId);
      msg.setPub(1);
      psnStatisticsRefreshMessageConsumer.receive(msg);;
    } catch (Exception e) {
      logger.error("人员统计信息更新服务成果编辑、导入、删除psnid=" + psnId, e);
    }
  }

}
