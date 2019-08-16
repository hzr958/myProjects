package com.smate.center.mail.client.service;

import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;

/**
 * 邮件发送服务
 * 
 * @author tsz
 *
 */
public interface MailClientSendService {

  /**
   * 发送邮件
   * 
   * @param mailInfo
   */
  public void execute(String mailInfo);

  /**
   * 更新状态
   * 
   * @param mailId
   * @param statsus
   * @param msg
   */
  public void updateMailStatus(Long mailId, MailSendStatusEnum statsus, String msg);
}
