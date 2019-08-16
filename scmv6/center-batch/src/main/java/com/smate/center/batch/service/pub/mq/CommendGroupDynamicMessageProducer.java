package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 群组用 生成动态共用入口消息发送者2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
@Component("commendGroupDynamicMessageProducer")
public class CommendGroupDynamicMessageProducer {

  @Autowired
  private CommendGroupDynamicMessageConsumer commendGroupDynamicMessageConsumer;
  private static Logger logger = LoggerFactory.getLogger(CommendGroupDynamicMessageProducer.class);

  /**
   * @param message
   * @return int 0:同步消息不完整; -1:同步失败; 1：同步成功;
   * @throws ServiceException
   */
  public int syncCommendDynamicMessage(CommendDynamicMessage message) throws ServiceException {
    try {
      logger.debug("进入生成群组动态消息发送!");
      commendGroupDynamicMessageConsumer.receive(message);;
    } catch (ServiceException e) {
      logger.error("生成群组动态消息发送失败!", e);
      throw new ServiceException(e);
    }
    return 1;
  }

}
