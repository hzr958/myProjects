package com.smate.center.batch.service.pub.mq;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.mail.EmailHandlerService;

/**
 * 
 * 同步邮件到邮件服务消费者
 * 
 * @author zk
 * 
 */
@Component("syncMailToMailSrvConsumer")
public class SyncMailToMailSrvConsumer {

  @Resource(name = "emailSyncFlagHandler")
  EmailHandlerService emailHandler;

  public void receive(SyncMailToMailSrvMessage message) throws ServiceException {
    System.out.println("有同步邮件来了");
    emailHandler.handler(message);
  }
}
