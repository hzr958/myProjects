package com.smate.center.batch.service.pub.mq;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 本地消息消费者接口.
 * 
 * @author liqinghua
 * 
 */
public interface ILocalQueneConsumer {

  public void receive(BaseLocalQueneMessage message) throws ServiceException;
}
