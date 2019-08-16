package com.smate.core.base.utils.model.cas.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 记录用户每次的登录信息
 * 
 * @author weilong peng
 *
 */
@Entity
@Table(name = "SYS_USER_LOGIN_LOG")
public class SysUserLoginLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4416983100188622897L;

  /** 主键. */
  private Long id;
  /** 用户id. */
  private Long psnId;
  /** 登录时间. */
  private Date loginTime;
  /** 登录ip. */
  private String loginIp;
  /** 登录系统根路径. */
  private String sysRootPath;
  /** 登录类型 */
  private Integer loginType;
  /** 浏览器版本信息 */
  private String browserInfo;
  /** 操作系统版本信息 */
  private String systemInfo;

  public SysUserLoginLog() {}

  public SysUserLoginLog(String loginIp, String sysRootPath) {
    this.loginIp = loginIp;
    this.sysRootPath = sysRootPath;
  }

  @Column(name = "BROWSER_INFO")
  public String getBrowserInfo() {
    return browserInfo;
  }

  public void setBrowserInfo(String browserInfo) {
    this.browserInfo = browserInfo;
  }

  @Column(name = "SYSTEM_INFO")
  public String getSystemInfo() {
    return systemInfo;
  }

  public void setSystemInfo(String systemInfo) {
    this.systemInfo = systemInfo;
  }

  public SysUserLoginLog(Long psnId, Date loginTime, String loginIp, String sysRootPath, Integer loginType) {
    this.psnId = psnId;
    this.loginTime = loginTime;
    this.loginIp = loginIp;
    this.sysRootPath = sysRootPath;
    this.loginType = loginType;
  }

  public SysUserLoginLog(Long psnId, Date loginTime, String loginIp, String sysRootPath, Integer loginType,
      String browserInfo, String systemInfo) {
    super();
    this.psnId = psnId;
    this.loginTime = loginTime;
    this.loginIp = loginIp;
    this.sysRootPath = sysRootPath;
    this.loginType = loginType;
    this.browserInfo = browserInfo;
    this.systemInfo = systemInfo;
  }

  public SysUserLoginLog(Long id, Long psnId, Date loginTime, String loginIp, String sysRootPath, Integer loginType,
      String browserInfo, String systemInfo) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.loginTime = loginTime;
    this.loginIp = loginIp;
    this.sysRootPath = sysRootPath;
    this.loginType = loginType;
    this.browserInfo = browserInfo;
    this.systemInfo = systemInfo;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_USER_LOGIN_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "LOGIN_TIME")
  public Date getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }

  @Column(name = "LOGIN_IP")
  public String getLoginIp() {
    return loginIp;
  }

  public void setLoginIp(String loginIp) {
    this.loginIp = loginIp;
  }

  @Column(name = "SYS_ROOT_PATH")
  public String getSysRootPath() {
    return sysRootPath;
  }

  public void setSysRootPath(String sysRootPath) {
    this.sysRootPath = sysRootPath;
  }

  @Column(name = "LOGIN_TYPE")
  public Integer getLoginType() {
    return loginType;
  }

  public void setLoginType(Integer loginType) {
    this.loginType = loginType;
  }

}
