package com.smate.center.batch.service.emailsimplify;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.emailsimplify.MailPromoteInfo;
import com.smate.core.base.utils.model.Page;


/**
 * 
 * 推广邮件相关信息业务接口
 * 
 * @author zk
 * 
 */
public interface PromoteEmailInfoService {

  Page<MailPromoteInfo> getMailTempInfo(Page<MailPromoteInfo> page) throws ServiceException;

  Boolean startGenerateMailData(Integer tempId) throws ServiceException;

  Boolean sendMailData(Integer tempId) throws ServiceException;

  Page<String> getMailContextForPreView(Integer tempId, Page<String> page) throws ServiceException;

}
