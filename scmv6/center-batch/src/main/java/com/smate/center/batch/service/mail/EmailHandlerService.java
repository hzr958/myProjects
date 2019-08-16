package com.smate.center.batch.service.mail;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 
 * 邮件处理基类－－责任链
 * 
 * @author zk
 * 
 */
public interface EmailHandlerService {

  String handler(Object... params) throws ServiceException;

}
