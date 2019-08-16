package com.smate.center.batch.service.mail;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 
 * @author tsz 邮件拦截
 * 
 */
public interface MailFilterService {

  /**
   * 邮件发送权限，判断收件人是否接收此类邮件
   * 
   * @param receivePsnId
   * @param templateName
   * @return
   */
  Boolean sendMailJurisdiction(Long receivePsnId, String templateName) throws ServiceException;

}
