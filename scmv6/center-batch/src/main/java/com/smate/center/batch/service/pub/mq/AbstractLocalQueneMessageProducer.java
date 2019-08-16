package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 单位这边部分需要插入数据库的临时处理办法.
 * 
 * @author liqinghua
 * 
 */
public abstract class AbstractLocalQueneMessageProducer {

  @Autowired
  private LocalQueueService localQueueService;

  public void sendMessage(BaseLocalQueneMessage message) throws ServiceException {

    Assert.notNull(message);
    localQueueService.createMsg(this.getCbeanName(), message);
  }

  public abstract String getCbeanName();
}
