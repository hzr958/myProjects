package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.JnlOATypeRefresh;

/**
 * 期刊-开放存储类型刷新SERVICE接口.
 * 
 * @author xys
 * 
 */
public interface JnlOATypeRefreshService extends Serializable {

  /**
   * 批量获取待刷新记录.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<JnlOATypeRefresh> getJnlOATypeNeedRefreshBatch(int maxSize) throws ServiceException;

  /**
   * 更新期刊-开放存储类型.
   * 
   * @param jnlOATypeRefresh
   * @throws ServiceException
   */
  void updateJnlOAType(JnlOATypeRefresh jnlOATypeRefresh) throws ServiceException;

  /**
   * 保存刷新记录.
   * 
   * @param jnlOATypeRefresh
   * @throws ServiceException
   */
  void saveJnlOATypeRefresh(JnlOATypeRefresh jnlOATypeRefresh) throws ServiceException;

  /**
   * 保存或更新刷新记录.
   * 
   * @param jid
   * @throws ServiceException
   */
  void saveOrUpdateJnlOATypeRefresh(Long jid);

  /**
   * 保存刷新记录.
   * 
   * @param jid
   */
  void saveJnlOATypeRefresh(Long jid);
}
