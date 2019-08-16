package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 生成动态共用入口消息发送者2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
@Component("commendDynamicMessageProducer")
public class CommendDynamicMessageProducer {
  @Autowired
  private CommendDynamicMessageConsumer commendDynamicMessageConsumer;
  private static Logger logger = LoggerFactory.getLogger(CommendDynamicMessageProducer.class);

  /**
   * @param message
   * @return int 0:同步消息不完整; -1:同步失败; 1：同步成功;
   * @throws ServiceException
   */
  public int syncCommendDynamicMessage(CommendDynamicMessage message) throws ServiceException {
    try {
      logger.debug("进入动态消息发送!");
      commendDynamicMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("生成动态消息发送失败!", e);
      throw new ServiceException(e);
    }
    return 1;
  }


}
