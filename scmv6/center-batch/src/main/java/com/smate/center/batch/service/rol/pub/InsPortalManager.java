package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.EntityManager;
import com.smate.core.base.utils.model.InsPortal;

/**
 * 
 * 
 * @author lqh
 * 
 */
public interface InsPortalManager extends EntityManager<InsPortal, Long> {

  /**
   * 获取单位节点.
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  Integer getInsNodeId(Long insId) throws ServiceException;

  /**
   * 通过域名获取单位域名等信息.
   */
  InsPortal getInsPortalByInsId(Long insId) throws ServiceException;
}
