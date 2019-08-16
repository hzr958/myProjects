package com.smate.center.mail.service;

import com.smate.center.mail.connector.mongodb.model.MailContent;

/**
 * 邮件内容记录服务
 * 
 * @author yhx
 *
 */
public interface MailContentService {

  /**
   * 获取邮件内容
   * 
   * @param mailId
   * @return
   */
  public MailContent getMailContentByMail(Long mailId);


}
