package com.smate.center.open.service.email;

import java.io.Serializable;

import com.smate.core.base.utils.model.security.Person;


/**
 * 科研之友发送邮件service.
 * 
 * @author pwl
 * 
 */
public interface ScmEmailService extends Serializable {

  /**
   * 获取用户接收邮件的语言.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  public String getReceiverLanguage(Person person) throws Exception;
}
