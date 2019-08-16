package com.smate.center.mail.client.service;

/**
 * 邮件发送线程
 * 
 * @author tsz
 *
 */
public class MailClientSendThreadServiceImpl implements Runnable {

  private MailClientSendService mailClientSendService;

  private String mailInfo;

  public MailClientSendThreadServiceImpl(MailClientSendService mailClientSendService, String mailInfo) {
    super();
    this.mailClientSendService = mailClientSendService;
    this.mailInfo = mailInfo;
  }

  @Override
  public void run() {
    mailClientSendService.execute(mailInfo);
  }

}
