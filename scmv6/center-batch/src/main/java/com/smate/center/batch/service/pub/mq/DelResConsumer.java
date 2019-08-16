package com.smate.center.batch.service.pub.mq;

import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 删除资源同步信息消费者 sns
 * 
 * @author Scy
 * 
 */
@Component("delResConsumer")
public class DelResConsumer {


  public void receive(DelResMessage message) throws ServiceException {

    // TODO
    // DelResMessage delResMessage = (DelResMessage) message;

    // 删除读者推荐记录
    // recomReaderStatisticsService.delReaderRecord(delResMessage.getActionKey(),
    // delResMessage.getActionType());
    // 删除群组推荐记录
    // recomGroupStatisticsService.delGroupRecord(delResMessage.getActionKey(),
    // delResMessage.getActionType());
    // 删除阅读记录
    // readStatisticsService.delReadRecord(delResMessage.getActionKey(),
    // delResMessage.getActionType());
  }

}
