package com.smate.core.web.sns.menu.service;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.InsPortal;


/**
 * 登录人员单位信息类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface InsPortalManager {

  /**
   * 通过域名获取单位域名等信息.
   */
  InsPortal getInsPortalByDomain(String domain) throws SysServiceException;

  /**
   * 通过域名获取单位域名等信息.
   */
  InsPortal getInsPortalByInsId(Long insId) throws SysServiceException;

  /**
   * 保存单位域名等信息.
   * 
   * @param insPortal
   * @throws ServiceException
   */
  void saveInsPortal(InsPortal insPortal) throws SysServiceException;

  /**
   * 同步单位域名等信息时保存单位保存.
   * 
   * @param insPortal
   * @throws ServiceException
   */
  void syncSaveInsPortal(InsPortal insPortal) throws SysServiceException;

  /**
   * 获取人员所有的Rol地址.
   * 
   * @param insId
   * @throws ServiceException
   */
  List<InsPortal> findUserRolUrl(Long psnId, Long insId) throws SysServiceException;

  /**
   * 修改单位LOGO.
   * 
   * @throws ServiceException
   */
  InsPortal saveInsLogo(Long insId, String path) throws SysServiceException;

  /**
   * 获取单位节点.
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  Integer getInsNodeId(Long insId) throws SysServiceException;

  /**
   * 同步V2.6数据.
   * 
   * @param oldData
   * @throws ServiceException
   */
  void syncInsPortalByOldInsPortal(Map<String, Object> oldData) throws SysServiceException;

  /**
   * 查找单位域名.
   * 
   * @param key
   * @return
   * @throws ServiceException
   */
  List<InsPortal> searchPortalByKey(String key) throws SysServiceException;

  /**
   * 获取单位域名列表.
   * 
   * @param insIds
   * @return
   * @throws ServiceException
   */
  List<InsPortal> getInsPortalByInsIds(List<Long> insIds) throws SysServiceException;
}
