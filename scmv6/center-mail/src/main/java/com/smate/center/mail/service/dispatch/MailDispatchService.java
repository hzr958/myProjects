package com.smate.center.mail.service.dispatch;

import com.smate.center.mail.model.MailDispatchInfo;

/**
 * 邮件调度服务接口
 * 
 * @author tsz
 *
 */
public interface MailDispatchService {

  /**
   * 执行方法 如果有问题 直接抛出异常
   */
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception;
}
