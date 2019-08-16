package com.smate.core.web.sns.menu.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.menu.ResourceDao;
import com.smate.core.base.utils.dao.menu.ResourceMappingDao;
import com.smate.core.base.utils.model.menu.Resource;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.web.sns.menu.model.MenuItemBean;


/**
 * 菜单服务类，用于提供标准的菜单数据.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("menuService")
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  protected ResourceBundleMessageSource messageSource;
  @Autowired
  private ResourceDao resourceDao;
  @Autowired
  private ResourceMappingDao resourceMappingDao;


  public MenuItemBean getMenuItems(Integer roleId) {

    // 用户编码
    Long psnId = SecurityUtils.getCurrentUserId();
    Long insId = TheadLocalInsId.getInsId();
    if (insId != null && insId == 0) {
      insId = null;
    }

    // 取的菜单资源
    List<Resource> items = this.resourceDao.getResourcesWithPsn(psnId, insId, roleId);
    MenuItemBean menuItem = new MenuItemBean();
    if (items.size() > 0) {
      // 设置顶级菜单
      menuItem.setMenuId(0L);
      // 递归遍历未附加菜单
      scanFreeMenuSourceForMenu(menuItem, items);
    } else {
      logger.warn("菜单数据未读取成功psn=" + psnId + ";insId=" + insId + ";roleId=" + roleId);
    }

    return menuItem;
  }

  public MenuItemBean getMenuItems(Integer roleId, Long snsInsId, Integer showMenuId) {

    MenuItemBean menuItem = getMenuItems(roleId);
    MenuItemBean menuChildren = new MenuItemBean();

    // 添加一个 ROL的研究人员在SNS时，多一个机构首页的菜单
    if (null != showMenuId) {
      menuChildren.setMenuId(Long.valueOf(showMenuId));

    }
    String menuName = "";
    if ("zh".equals(LocaleContextHolder.getLocale().getLanguage())) {
      menuName = messageSource.getMessage("机构首页", null, "机构首页", LocaleContextHolder.getLocale());
    } else {
      menuName = messageSource.getMessage("Home", null, "Home", LocaleContextHolder.getLocale());
    }

    menuChildren.setName(menuName);
    // 定位机构首页菜单，不要删
    menuChildren.setMenuId(9999L);
    // 新系统暂时只有成果在线需要皮肤变更，未来此处还会处理sie，暂默认设为此值
    menuChildren.setValue("/ronline/main");

    if (menuItem.getMenuItems() != null && menuItem.getMenuItems().size() > 0) {
      List<MenuItemBean> itemList = menuItem.getMenuItems();
      itemList.add(0, menuChildren);
    }

    return menuItem;
  }

  /**
   * 修正了递归遍历未附加菜单时引发的科研之友以外的系统菜单加载异常：菜单加载缺失部分项. 问题原因：当数据库表中未设置顶级菜单的父菜单ID值时，查询到的父菜单记录为空，程序进入死循环.
   * 
   * @author Mao JianGuo 2012-09-24
   */
  /**
   * 递归遍历未附加菜单.
   * 
   * @param menuItem
   * @param itemsTemp
   */
  private void scanFreeMenuSourceForMenu(MenuItemBean menuItem, List<Resource> itemsTemp) {

    List<Resource> tempLst = new ArrayList<Resource>();
    for (int itemIndex = 0; itemIndex < itemsTemp.size(); itemIndex++) {
      Resource currentItem = itemsTemp.get(itemIndex);
      // 获得需要添加子节点的父节点.
      Long parentMenuId = currentItem.getParentId().longValue();
      // 递归查询当前菜单的父菜单记录.
      MenuItemBean tmpItem = getMenuItem(menuItem, parentMenuId);
      if (tmpItem != null) {
        MenuItemBean menuChildren = new MenuItemBean();
        menuChildren.setMenuId(currentItem.getId());
        // 本地语言资源
        String menuName = messageSource.getMessage(currentItem.getName(), null, currentItem.getName(),
            LocaleContextHolder.getLocale());
        menuChildren.setName(menuName);
        menuChildren.setValue(currentItem.getValue());
        menuChildren.setTarget(currentItem.getTarget());
        // 在父菜单中加载子菜单.
        tmpItem.getMenuItems().add(menuChildren);
      } else {
        tempLst.add(itemsTemp.get(itemIndex));
      }

    }
    if (tempLst.size() > 0) {
      scanFreeMenuSourceForMenu(menuItem, tempLst);
    } else {
      return;
    }

  }

  /**
   * 导航菜单数据生成.
   * 
   * @param menuId menu id. Programmatically create a list of menu items.
   * @return tag .
   */
  public MenuItemBean getNavItems(Long menuId) {

    List<Resource> menus = this.resourceDao.getResourcesWithId(menuId);

    MenuItemBean menuItem = new MenuItemBean();
    for (int i = 0; i < menus.size(); i++) {
      MenuItemBean menuChildren = new MenuItemBean();

      menuChildren.setMenuId(menus.get(i).getId());
      String menuName = messageSource.getMessage(menus.get(i).getName(), null, menus.get(i).getName(),
          LocaleContextHolder.getLocale());

      menuChildren.setName(menuName);
      menuChildren.setValue(menus.get(i).getValue());
      menuChildren.setTarget(menus.get(i).getTarget());
      menuItem.getMenuItems().add(menuChildren);
    }

    return menuItem;
  }

  /**
   * 
   * 根据ID递归获得menu.
   * 
   * @param menu menu entity.
   * @param menuId menu id.
   * @return MenuItemBean
   */
  private MenuItemBean getMenuItem(MenuItemBean menu, long menuId) {

    MenuItemBean resMenu = null;

    if (menu.getMenuId() == menuId) {
      resMenu = menu;
    } else if (menu.getMenuItems().size() > 0) {

      for (int i = 0; i < menu.getMenuItems().size(); i++) {

        MenuItemBean itemMenu = (MenuItemBean) menu.getMenuItems().get(i);
        if (itemMenu.getMenuId().longValue() == menuId) {
          resMenu = itemMenu;
        } else {
          MenuItemBean tmp = getMenuItem(itemMenu, menuId);
          if (tmp != null) {
            resMenu = tmp;
          }
        }
      }
    }

    return resMenu;
  }

  /**
   * 通过请求的URL路径获得menuId，便于自动切换菜单展现.
   * 
   * @author kexitang
   * 
   * @param reqStr request URL
   * @return menuId
   */
  public String getMenuIdByRequestURL(String reqStr) {
    String menuIdStr = null;
    // 根据请求URL匹配到菜单记录.
    List<Long> menuIdList = this.resourceDao.getMenuIdListByUrl(reqStr);
    if (CollectionUtils.isNotEmpty(menuIdList)) {
      menuIdStr = ObjectUtils.toString(menuIdList.get(0));
    } else {
      // 根据请求URL没有匹配到菜单.
      Long menuId = this.resourceMappingDao.getMenuIdByUrl(reqStr);
      if (menuId != null) {
        // 如果该菜单是1-启用状态，则显示.
        Resource resource = this.resourceDao.get(menuId);
        if (resource != null && resource.getStatus().intValue() > 0) {
          menuIdStr = menuId.toString();
        }
      }
    }
    return menuIdStr;

  }

  /**
   * 根据角色和请求URL查找匹配的菜单ID.
   * 
   * @param roleId
   * @param reqUrl
   * @return
   */
  public String getMenuIdByUrlAndRole(Integer roleId, String reqUrl) {
    String menuIdStr = null;
    Long psnId = SecurityUtils.getCurrentUserId();
    Long insId = TheadLocalInsId.getInsId();
    // 根据请求URL匹配到菜单记录.
    List<Long> menuIdList = this.resourceDao.getMenuIdListByRoleAndUrl(reqUrl, psnId, roleId.longValue(), insId);
    if (CollectionUtils.isNotEmpty(menuIdList)) {
      menuIdStr = String.valueOf(menuIdList.get(0));
    } else {
      // 根据请求URL没有匹配到菜单.
      Long menuId = this.resourceMappingDao.getMenuIdByUrlAndRole(roleId.longValue(), reqUrl);
      if (menuId != null) {
        // 如果该菜单是1-启用状态，则显示.
        Resource resource = this.resourceDao.get(menuId);
        if (resource != null && resource.getStatus().intValue() > 0) {
          menuIdStr = menuId.toString();
        }
      }
    }
    return menuIdStr;
  }

  @SuppressWarnings("rawtypes")
  public List getAllMenus() {
    return this.resourceDao.getAllMenus();

  }

  /**
   * 验证请求URL是否有权限
   */
  @Override
  public boolean hasPrivilegeForUrl(Integer roleId, String url) {

    boolean hasRight = false;
    String menuIdStr = null;
    // 根据角色和请求URL获取匹配的菜单ID.
    if (roleId != null && roleId.intValue() > 0) {
      menuIdStr = this.getMenuIdByUrlAndRole(roleId, url);
    } else {
      menuIdStr = this.getMenuIdByRequestURL(url);
    }
    if (StringUtils.isNotBlank(menuIdStr)) {
      hasRight = true;
    }

    return hasRight;
  }

  /**
   * 获取当前登录用户的菜单列表.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public List getCurUserMenus(Integer roleId) {
    // 用户编码
    Long psnId = SecurityUtils.getCurrentUserId();
    Long insId = TheadLocalInsId.getInsId();
    List menuIds = new ArrayList();
    // 取的菜单资源
    List<Resource> items = this.resourceDao.getResourcesWithPsn(psnId, insId, roleId);
    if (items != null) {
      for (Resource res : items) {
        menuIds.add(new Object[] {res.getId(), res.getValue()});
      }
    }

    return menuIds;
  }

  /**
   * 清除请求URL中的参数.
   * 
   * @param reqUrl
   * @return
   */
  private String formatReqUrl(String reqUrl) {
    if (StringUtils.isNotBlank(reqUrl)) {
      reqUrl = StringUtils.replace(reqUrl, "http://", "");
      reqUrl = StringUtils.substring(reqUrl, reqUrl.indexOf("/"));
      if (reqUrl.contains("?")) {
        reqUrl = StringUtils.substringBefore(reqUrl, "?");
      }
    }
    return reqUrl;
  }
}
