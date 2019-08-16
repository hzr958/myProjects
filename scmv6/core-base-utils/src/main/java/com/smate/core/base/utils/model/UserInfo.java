package com.smate.core.base.utils.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 自定义的用户安全令牌信息，添加所属nodeId.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class UserInfo extends User {

  private static final long serialVersionUID = -5044442380955114204L;
  /**
   * 用户个人数据库节点位置.
   */
  private int nodeId;
  /**
   * 用户在ROL程序中当前登录的单位站点,ROL专用.
   */
  private Long insId;

  /**
   * 用户在ROL 或者 NSFCROL 程序中当前登录的角色,ROL专用.
   */
  private Integer roleId;

  /**
   * 用户在ROL 或者 NSFCROL 程序中所拥有的角色,ROL专用.
   */
  private List roleIds;

  /**
   * 注册进来的中文名字/注册进来的英文版本,用于界面显示.
   */
  private String name;
  /**
   * 用户信息所在系统
   */
  private String sys;

  public UserInfo(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities, int nodeId)
      throws IllegalArgumentException {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.nodeId = nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  public int getNodeId() {
    return nodeId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleIds(List roleIds) {
    this.roleIds = roleIds;
  }

  public List getRoleIds() {
    return roleIds;
  }

  public String getSys() {
    return sys;
  }

  public void setSys(String sys) {
    this.sys = sys;
  }

}
