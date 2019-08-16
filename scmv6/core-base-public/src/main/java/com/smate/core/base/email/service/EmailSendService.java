package com.smate.core.base.email.service;

import com.smate.core.base.utils.exception.PublicException;

/**
 * 邮件发送接口
 * 
 * @author zk
 *
 */
public interface EmailSendService {

  public void syncEmailInfo(Object... params) throws PublicException;
}
