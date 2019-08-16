package com.smate.core.web.sns.menu.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.web.sns.menu.model.MenuItemBean;
import com.smate.core.web.sns.menu.utils.MenuUtils;

/**
 * 菜单客户端基础类.
 * 
 * @author mjg.
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractMenuClientService implements MenuClientService {

  private static final long serialVersionUID = -6642907198646394253L;

  /**
   * 缓存不存在数据时，需要获取数据库菜单配置.
   * 
   * @param userInfo
   * @return
   * @throws SysServiceException
   */
  public abstract MenuItemBean getDbMenuItemBean(Map userInfo) throws SysServiceException;

  /**
   * 根据请求的角色和请求链接获取匹配的菜单ID.
   * 
   * @param userInfo
   * @param reqUrl
   * @return
   */
  public abstract String getMenuIdByUrlAndRole(Map userInfo, String reqUrl);

  /**
   * 获取菜单对象.
   * 
   * @param userInfo
   * @return
   * @throws SysServiceException
   */
  public MenuItemBean getMenuItem(Map userInfo) throws SysServiceException {

    // 获取缓存中的菜单，如果缓存菜单为空，则查询数据库获取菜单数据.
    MenuItemBean item = this.getDbMenuItemBean(userInfo);
    return item;
  }

  /**
   * 获取选定的菜单ID.
   * 
   * @param url
   * @return
   * @throws SysServiceException
   */
  public Integer getMenuIdByUrl(String url, Map userInfo) throws SysServiceException {
    // 增加菜单匹配逻辑_MJG_2014-07-23_CITYU-109.
    String targetMenuId = this.getMenuIdByUrlAndRole(userInfo, url);
    if (StringUtils.isNotBlank(targetMenuId)) {
      return Integer.valueOf(targetMenuId);
    }
    // 获取菜单列表.
    List<MenuItemBean> menuItemList = this.getMenuItemList(userInfo);
    if (menuItemList != null) {
      // 判断请求地址中是否包含参数或.action内容(true-包含；false-不包含).
      boolean flag = (url.indexOf("?") > 0 || url.indexOf(".") > 0);
      // 遍历菜单列表，获取对应URL的菜单并取其ID.
      for (MenuItemBean item : menuItemList) {
        if (StringUtils.isNotBlank(item.getValue())) {
          String iUrl = item.getValue();
          // 如果请求地址中不包含参数和.acion 内容，则将截取数据库保存的url地址，删除参数和.acion
          // 内容_MaoJianGuo_2012-12-12_ROL-4.
          if (iUrl.indexOf("?") > 0 && !flag) {
            iUrl = iUrl.substring(0, iUrl.indexOf("?"));
          }
          if (iUrl.indexOf(".") > 0 && !flag) {
            iUrl = iUrl.substring(0, iUrl.indexOf("."));
          }
          if (iUrl.equals(url)) {
            return item.getMenuId().intValue();
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取菜单列表.
   * 
   * @param userInfo
   * @return
   * @throws SysServiceException
   */
  private List<MenuItemBean> getMenuItemList(Map userInfo) throws SysServiceException {

    // 根据菜单数据获取菜单列表.
    MenuItemBean menuItems = this.getMenuItem(userInfo);
    List<MenuItemBean> menuItesList = MenuUtils.getMenuItemList(menuItems);
    // 反向，从子菜单开始找
    Collections.reverse(menuItesList);
    List<MenuItemBean> itemList = menuItesList;

    return itemList;

  }

}
