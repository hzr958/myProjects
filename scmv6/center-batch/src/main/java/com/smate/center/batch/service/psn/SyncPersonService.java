package com.smate.center.batch.service.psn;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.model.security.Person;



public interface SyncPersonService {

  /**
   * 根据节点获取域名
   * 
   * @param nodeId
   * @return
   * @throws ServiceException
   */
  String getDomainUrlByNodeId(Integer nodeId) throws ServiceException;

  /**
   * 获取系统管理员
   * 
   * @return
   * @throws ServiceException
   */
  Person loadSystemPerson() throws ServiceException;

  /**
   * 科研简历url
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String buildResumeUrl(Long psnId) throws ServiceException;

}
