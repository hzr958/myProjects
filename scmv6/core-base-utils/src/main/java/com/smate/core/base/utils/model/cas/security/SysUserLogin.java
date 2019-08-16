package com.smate.core.base.utils.model.cas.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 用户是否登陆过记录表.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "SYS_USER_LOGIN")
public class SysUserLogin implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8430707699192344170L;

  /**
   * 主健.
   */
  private Long id;

  private Date lastLoginTime;

  private Long selfLogin;

  // 登录的IP
  private String loginIP;

  private Date lastPwdChanged;

  private Integer syncFlag = 0; // 是否同步至sns/sie/rcmd et.标记

  public SysUserLogin() {
    super();
    lastLoginTime = new Date();

  }

  public SysUserLogin(Long id) {
    super();
    this.id = id;
    lastLoginTime = new Date();
  }

  public SysUserLogin(Long id, Long selfLogin) {
    super();
    this.id = id;
    this.lastLoginTime = new Date();
    this.selfLogin = selfLogin;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "LAST_LOGIN_TIME")
  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  @Column(name = "SELF_LOGIN")
  public Long getSelfLogin() {
    return selfLogin;
  }

  public void setSelfLogin(Long selfLogin) {
    this.selfLogin = selfLogin;
  }

  @Column(name = "LOGIN_IP")
  public String getLoginIP() {
    return loginIP;
  }

  public void setLoginIP(String loginIP) {
    this.loginIP = loginIP;
  }

  @Column(name = "LAST_PWD_CHANGED")
  public Date getLastPwdChanged() {
    return lastPwdChanged;
  }

  public void setLastPwdChanged(Date lastPwdChanged) {
    this.lastPwdChanged = lastPwdChanged;
  }

  @Column(name = "SYNC_FLAG")
  public Integer getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(Integer syncFlag) {
    this.syncFlag = syncFlag;
  }



}
