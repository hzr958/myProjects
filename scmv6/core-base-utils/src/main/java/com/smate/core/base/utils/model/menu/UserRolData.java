package com.smate.core.base.utils.model.menu;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.InsPortal;

/**
 * session中菜单封装类.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class UserRolData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5768362704276012299L;

  /** 切换对应的单位图标或者界面. */
  private Long rolInsId;
  /** 对应单位的域名,ins_portal表. */
  private String rolDomain;
  private String rolLogoUrl;
  private String rolTitle;
  private boolean rolMultiRole = false;
  private List<InsPortal> portal;
  // 当前域名单位的对应portal的索引数.
  private Integer portalIndex;
  /** 单位个数，如果不为null，则表示已经读取过portal列表，避免多次读取portal. */
  private Integer portalCount;
  /** 角色个数 */
  private Integer roleCount;

  /** 标记从哪儿跳过来的，并且根据这个值确定显示什么样单位的图标在顶部，并且在sns的菜单加入一个机构菜单. */
  private String from;

  /** 呈现在界面上的姓名(中文). */
  private String username;
  /** 呈现在界面上的姓名(英文). */
  private String enUsername;
  private String firstName;
  private String avatars;

  private String lastName;

  private String des3PsnId;
  private String fromIsis = "";

  /** 当前显示的菜单menuId. */
  private int menuId;
  // 是否显示消息提示
  private Map<String, Object> msgData = null;
  // 是否发送登录信息同步到单位的猥琐标记（LQH）
  private Integer isSyncLogin = 0;

  // 用户当前登录的角色(科研在线系统用到)_MaoJianGuo_2012-12-07_ROL-4 .
  private String userRolId;
  // 机构科研在线登陆页面的图标显示其他语言版本的内容(相对当前语言版本内容而言)_MaoJianGuo_2012-12-07_ROL-318 .
  private String rolTitleEn;
  private String rolTitleCh;
  private Long sieVersion = 0L;
  private String currentContext;// 当前上下文，用于不同系统差异化处理

  public Long getRolInsId() {
    return rolInsId;
  }

  public String getRolLogoUrl() {
    return rolLogoUrl;
  }

  public List<InsPortal> getPortal() {
    return portal;
  }

  public void setRolInsId(Long rolInsId) {
    this.rolInsId = rolInsId;
  }

  public void setRolLogoUrl(String rolLogoUrl) {
    this.rolLogoUrl = rolLogoUrl;
  }

  public void setPortal(List<InsPortal> portal) {
    this.portal = portal;
  }

  public Integer getPortalCount() {
    return portalCount;
  }

  public void setPortalCount(Integer portalCount) {
    this.portalCount = portalCount;
  }

  public String getRolTitle() {
    return rolTitle;
  }

  public void setRolTitle(String rolTitle) {
    this.rolTitle = rolTitle;
  }

  public void setPortalIndex(Integer portalIndex) {
    this.portalIndex = portalIndex;
  }

  public Integer getPortalIndex() {
    return portalIndex;
  }

  /** @return the from */
  public String getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(String from) {
    this.from = from;
  }

  public void setMenuId(int menuId) {
    this.menuId = menuId;
  }

  public int getMenuId() {
    return menuId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public String getEnUsername() {
    return enUsername;
  }

  public void setEnUsername(String enUsername) {
    this.enUsername = enUsername;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Map<String, Object> getMsgData() {
    return msgData;
  }

  public void setMsgData(Map<String, Object> msgData) {
    this.msgData = msgData;
  }

  /** @return the fromIsis */
  public String getFromIsis() {
    return fromIsis;
  }

  /**
   * @param fromIsis the fromIsis to set
   */
  public void setFromIsis(String fromIsis) {
    this.fromIsis = fromIsis;
  }

  public Integer getIsSyncLogin() {
    return isSyncLogin;
  }

  public void setIsSyncLogin(Integer isSyncLogin) {
    this.isSyncLogin = isSyncLogin;
  }

  public void setRolDomain(String rolDomain) {
    this.rolDomain = rolDomain;
  }

  public String getRolDomain() {
    return rolDomain;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public boolean isRolMultiRole() {
    return rolMultiRole;
  }

  public void setRolMultiRole(boolean rolMultiRole) {
    this.rolMultiRole = rolMultiRole;
  }

  public String getUserRolId() {
    return userRolId;
  }

  public void setUserRolId(String userRolId) {
    this.userRolId = userRolId;
  }

  public String getRolTitleEn() {
    return rolTitleEn;
  }

  public String getRolTitleCh() {
    return rolTitleCh;
  }

  public void setRolTitleEn(String rolTitleEn) {
    this.rolTitleEn = rolTitleEn;
  }

  public void setRolTitleCh(String rolTitleCh) {
    this.rolTitleCh = rolTitleCh;
  }

  public Long getSieVersion() {
    return sieVersion;
  }

  public void setSieVersion(Long sieVersion) {
    this.sieVersion = sieVersion;
  }

  public String getCurrentContext() {
    return currentContext;
  }

  public void setCurrentContext(String currentContext) {
    this.currentContext = currentContext;
  }

  public Integer getRoleCount() {
    return roleCount;
  }

  public void setRoleCount(Integer roleCount) {
    this.roleCount = roleCount;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

}
