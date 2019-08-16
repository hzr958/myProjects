package com.smate.core.web.sns.menu.service;

import java.io.Serializable;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.web.sns.menu.model.MenuItemBean;

/**
 * 菜单客户端.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@SuppressWarnings("rawtypes")
public interface MenuClientService extends Serializable {

  // 选定菜单列表缓存的追加KEY值。
  public final static String LIST_KEY_END = "reverse";

  /**
   * 获取菜单对象.
   * 
   * @param userInfo
   * @return
   * @throws SysServiceException
   */
  public MenuItemBean getMenuItem(Map userInfo) throws SysServiceException;


  /**
   * 获取选定菜单ID.
   * 
   * @param url
   * @param userInfo
   * @return
   * @throws SysServiceException
   */
  public Integer getMenuIdByUrl(String url, Map userInfo) throws SysServiceException;
}
