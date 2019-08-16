package com.smate.center.mail.service.build;

import com.smate.center.mail.model.MailDataInfo;

/**
 * 构造邮件信息服务接口
 * 
 * @author zzx
 *
 */
public interface MailBuildInfoExcuteService {
  /**
   * 执行方法 如果有问题 直接抛出异常
   */
  void excute(MailDataInfo info) throws Exception;;

}
