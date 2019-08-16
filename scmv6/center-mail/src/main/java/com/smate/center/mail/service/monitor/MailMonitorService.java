package com.smate.center.mail.service.monitor;

/**
 * 邮件调度监控
 * 
 * @author tsz
 *
 */
public interface MailMonitorService {

  /**
   * 发送监控邮件
   * 
   * @param mailInfo
   */
  public void sendMonitorMail(String subject, String content);
}
