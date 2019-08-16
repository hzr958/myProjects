package com.smate.center.mail.service;

/**
 * 邮件监控接口
 * 
 * @author zzx
 *
 */
public interface MailMonitoringService {
  /**
   * 一个psnid一个小时内产生了 20个邮件
   */
  void checkData1() throws Exception;

  /**
   * 一个接收邮箱一个小时内接收了20个邮件
   */
  void checkData2() throws Exception;

}
