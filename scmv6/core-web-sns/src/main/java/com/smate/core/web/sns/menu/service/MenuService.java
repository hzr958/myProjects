package com.smate.core.web.sns.menu.service;

import java.util.List;

import com.smate.core.web.sns.menu.model.MenuItemBean;



/**
 * 菜单服务类，用于提供标准的菜单数据.
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface MenuService {

  /**
   * 
   * 获得菜单列表，如果传递用户角色参数，则只获得该角色所拥有的菜单.
   * 
   * @return MenuItemBean.
   * 
   */
  MenuItemBean getMenuItems(Integer roleId);

  /**
   * 获得菜单列表，如果传递用户角色参数，则只获得该角色所拥有的菜单，如果传递单位Id，只获得在该单位权限范围内的菜单，并且在菜单添加一个机构首页.
   * 
   * @param roleId
   * @param insId
   * @param showMenuId
   * @return
   */
  MenuItemBean getMenuItems(Integer roleId, Long insId, Integer showMenuId);

  /**
   * 
   * Programmatically create a list of menu items.
   * 
   * @param menuId menu id.
   * @return MenuItemBean.
   * 
   */
  MenuItemBean getNavItems(Long menuId);

  /**
   * 通过请求的URL路径获得menuId，便于自动切换菜单展现.
   * 
   * @param reqURI
   * @return
   */
  String getMenuIdByRequestURL(String reqStr);

  /**
   * 根据角色和请求URL查找匹配的菜单ID.
   * 
   * @param roleId
   * @param reqUrl
   * @return
   */
  String getMenuIdByUrlAndRole(Integer roleId, String reqUrl);

  /**
   * 获得所有菜单值和url对应关系
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  List getAllMenus();

  /**
   * 获取当前用户的菜单列表.
   * 
   * @param roleId
   * @return
   */
  @SuppressWarnings("rawtypes")
  List getCurUserMenus(Integer roleId);

  /**
   * 判断当前角色是否有访问菜单的角色
   */
  boolean hasPrivilegeForUrl(Integer roleId, String url);

}
