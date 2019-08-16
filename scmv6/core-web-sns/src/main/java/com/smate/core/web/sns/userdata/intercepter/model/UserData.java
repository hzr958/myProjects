package com.smate.core.web.sns.userdata.intercepter.model;

import java.io.Serializable;

/**
 * 页面初始化 用户数据
 * 
 * @author tsz
 *
 */
public class UserData implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4119993349172399632L;
  /**
   * 切换对应的单位图标或者界面.
   */
  private Long rolInsId = 0L;
  /**
   * 对应单位的域名,ins_portal表.
   */
  private String rolDomain;
  private String rolLogoUrl;
  private String rolTitle;

  /**
   * 呈现在界面上的姓名(中文).
   */
  private String username;
  /**
   * 呈现在界面上的姓名(英文).
   */
  private String enUsername;

  /**
   * 头像地址
   */
  private String avatars;

  private String des3PsnId;

  private String sys = "SNS"; // 登录系统 (SNS,SIE,BPO.....)

  /**
   * 当前显示的菜单menuId.
   */
  private int menuId;

  private boolean rolMultiRole; // 多个 sie角色

  public Long getRolInsId() {
    return rolInsId;
  }

  public void setRolInsId(Long rolInsId) {
    this.rolInsId = rolInsId;
  }

  public String getRolDomain() {
    return rolDomain;
  }

  public void setRolDomain(String rolDomain) {
    this.rolDomain = rolDomain;
  }

  public String getRolLogoUrl() {
    return rolLogoUrl;
  }

  public void setRolLogoUrl(String rolLogoUrl) {
    this.rolLogoUrl = rolLogoUrl;
  }

  public String getRolTitle() {
    return rolTitle;
  }

  public void setRolTitle(String rolTitle) {
    this.rolTitle = rolTitle;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEnUsername() {
    return enUsername;
  }

  public void setEnUsername(String enUsername) {
    this.enUsername = enUsername;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public int getMenuId() {
    return menuId;
  }

  public void setMenuId(int menuId) {
    this.menuId = menuId;
  }

  public String getSys() {
    return sys;
  }

  public void setSys(String sys) {
    this.sys = sys;
  }

  public boolean getRolMultiRole() {
    return rolMultiRole;
  }

  public void setRolMultiRole(boolean rolMultiRole) {
    this.rolMultiRole = rolMultiRole;
  }

}
