package com.smate.center.batch.service.emailsimplify;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.emailsimplify.MailPromoteAuditLog;
import com.smate.center.batch.model.emailsimplify.MailPromoteStatLog;
import com.smate.core.base.utils.model.Page;


/**
 * 推广邮件相关日志远程调用服务类
 * 
 * @author zk
 * 
 */
public interface PromoteRelatedLogRemotingService {

  /**
   * 通过模板id得到推广邮件审核日志记录
   * 
   * @param tempId
   * @return
   * @throws ServiceException
   */
  Page<MailPromoteAuditLog> findMailPromoteAuditLogByTempId(Integer tempId, Page<MailPromoteAuditLog> auditPage)
      throws ServiceException;

  /**
   * 通过模板id得到推广邮件统计日志记录
   * 
   * @param tempId
   * @return
   * @throws ServiceException
   */
  Page<MailPromoteStatLog> findMailPromoteStatLogByTempId(Integer tempId, Page<MailPromoteStatLog> statPage)
      throws ServiceException;

}
