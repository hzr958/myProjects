package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 项目相关成果刷新同步消息生产者.
 * 
 * @author xys
 * 
 */
@Component("prjRelatedPubRefreshMessageProducer")
public class PrjRelatedPubRefreshMessageProducer {

  @Autowired
  private PrjRelatedPubRefreshMessageConsumer prjRelatedPubRefreshMessageConsumer;
  private static Logger logger = LoggerFactory.getLogger(PrjRelatedPubRefreshMessageProducer.class);


  public void refreshPrjRelatedPub(Long prjId, Long pubId, Long psnId, Integer refreshSource, Integer status)
      throws ServiceException {
    try {
      PrjRelatedPubRefreshMessage message = new PrjRelatedPubRefreshMessage(SecurityUtils.getCurrentAllNodeId().get(0),
          prjId, pubId, psnId, refreshSource, status);
      prjRelatedPubRefreshMessageConsumer.receive(message);
    } catch (Exception e) {
      logger.error("项目相关成果刷新同步消息的MQ发送失败！", e);
    }
  }

}
