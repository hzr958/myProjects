package com.smate.core.web.sns.menu.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.web.sns.menu.model.MenuItemBean;


/**
 * 科研之友WEB菜单服务.
 * 
 * @author mjg
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class SnsMenuClientServiceImpl extends AbstractMenuClientService {

  /**
   * 
   */
  private static final long serialVersionUID = -2809984170094464275L;
  // 菜单数据缓存的KEY值.
  private final static String KEY_PATTEN = "sns_menu_${locale}_${ins_id}";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MenuService menuService;

  /**
   * 从数据库获取菜单数据.
   */
  @SuppressWarnings("rawtypes")
  @Override
  public MenuItemBean getDbMenuItemBean(Map userInfo) throws SysServiceException {

    try {
      Long insId = (Long) userInfo.get("ins_id");
      Integer roleId = (Integer) userInfo.get("role_id");
      Integer showMenuId = null;
      if (insId != null && (insId == 2566 || insId == 2565)) {
        showMenuId = 9999;
        return menuService.getMenuItems(roleId, insId, showMenuId);
      } else {
        return menuService.getMenuItems(roleId);
      }
    } catch (Exception e) {
      logger.error("SNS获取菜单数据错误", e);
      throw new SysServiceException("SNS获取菜单数据错误", e);
    }
  }

  public void setMenuService(MenuService menuService) {
    this.menuService = menuService;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String getMenuIdByUrlAndRole(Map userInfo, String reqUrl) {
    Integer roleId = (Integer) userInfo.get("role_id");
    if (roleId != null && roleId.intValue() > 0) {
      return menuService.getMenuIdByUrlAndRole(roleId, reqUrl);
    } else {
      return menuService.getMenuIdByRequestURL(reqUrl);
    }
  }


}
