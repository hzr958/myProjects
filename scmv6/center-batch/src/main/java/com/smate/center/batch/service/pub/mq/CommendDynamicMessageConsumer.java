package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.CommendDynamicMessageService;

/**
 * 生成动态共用入口消息接收者. 2015.1.12_scm-6454 sns
 * 
 * @author tsz
 * 
 */
@Component("commendDynamicMessageConsumer")
public class CommendDynamicMessageConsumer {

  private static Logger logger = LoggerFactory.getLogger(CommendDynamicMessageConsumer.class);
  @Autowired
  private CommendDynamicMessageService commendDynamicMessageService;

  public void receive(CommendDynamicMessage message) throws ServiceException {
    try {
      CommendDynamicMessage commendDynMessage = (CommendDynamicMessage) message;
      // 以消息的形式 异步调用动态生成共用入口 tsz_2015.1.12_scm-6454
      commendDynamicMessageService.produceDynamic(commendDynMessage.getJsonObject(), commendDynMessage.getDynJson(),
          commendDynMessage.getExtFlag());
    } catch (Exception e) {
      logger.error("生成动态共用入口消息出错了!", e);
      throw new ServiceException(e);
    }
  }

}
