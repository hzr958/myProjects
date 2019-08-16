package com.smate.core.web.sns.menu.service;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * logo服务类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface MainInitialService {

  /**
   * @author oyh load logo for menuFilter
   * @param insId
   * @param fileFix
   * @return
   * @throws ServiceException
   */
  String[] loadMainLogo(Long insId) throws SysServiceException;

  /**
   * @author oyh load logo for RO login
   * @param insId
   * @param fileFix
   * @return
   * @throws ServiceException
   */
  String loadRolMainLogo(Long insId) throws SysServiceException;

  /**
   * 
   * @param domainURL
   * @return
   * @throws ServiceException
   */
  String[] loadRolMainLogoByDomain(String domainURL) throws SysServiceException;

}
