
package com.smate.core.web.sns.menu.model;

import java.io.Serializable;
import java.util.List;


/**
 * menu获取 对象 Represents a very simple model of a menu item
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class MenuItemBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2604259783779944648L;

  private Long menuId;
  private Long orderNum;
  private Long status;
  private Long level;
  private Long parentId;

  private MenuItemBean parentItem;
  private String name;
  private String value;
  private String target;
  private String resourceType;

  private List<MenuItemBean> menuItems;


  public MenuItemBean() {
    super();
  }

  public MenuItemBean(String name) {
    this.name = name;
  }

  public MenuItemBean(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public MenuItemBean(String name, String value, String target) {
    this.name = name;
    this.value = value;
    this.target = target;
  }


  public MenuItemBean(Long menuId, String name, String value, String target, Long level, List<MenuItemBean> menuItems) {
    this.menuId = menuId;
    this.name = name;
    this.value = value;
    this.target = target;
    this.level = level;
    this.menuItems = menuItems;
  }


  public Long getMenuId() {
    return menuId;
  }

  public void setMenuId(Long menuId) {
    this.menuId = menuId;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  /**
   * @param parentItem the parentItem to set
   */
  public void setParentItem(MenuItemBean parentItem) {
    this.parentItem = parentItem;
  }


  public MenuItemBean getParentItem() {
    return parentItem;
  }


  public Long getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Long orderNum) {
    this.orderNum = orderNum;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }


  public List<MenuItemBean> getMenuItems() {
    if (menuItems == null)
      menuItems = new java.util.ArrayList<MenuItemBean>(); // Collections.EMPTY_LIST;
    return menuItems;
  }

  public void setMenuItems(List<MenuItemBean> menuItems) {
    this.menuItems = menuItems;
  }



  /**
   * @return level
   */
  public Long getLevel() {
    return level;
  }

  /**
   * @param level 要设置的 level
   */
  public void setLevel(Long level) {
    this.level = level;
  }
}
