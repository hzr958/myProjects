package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;



/**
 * 
 * 同步邮件信息至邮件服务消息产生者
 * 
 * @author zk
 * 
 */

@Component(value = "syncMailToMailSrvProducer")
public class SyncMailToMailSrvProducer {

  @Autowired
  private SyncMailToMailSrvConsumer SyncMailToMailSrvConsumer;
  private String queueName = "syncMailToMailSrv";

  public void syncMessage(SyncMailToMailSrvMessage message) throws ServiceException {
    // message.setFromNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
    // this.sendMessage(message);
    SyncMailToMailSrvConsumer.receive(message);
  }

}
