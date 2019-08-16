package com.smate.center.merge.model.cas.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户.
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_USER")
public class Account implements Serializable {
  private static final long serialVersionUID = -302924477116101106L;
  private Long id; // user id
  private String loginName; // 登录用户名
  private String password; // 使用MD5保存密码.
  private Boolean enabled;
  private Short tokenChanged; // TOKEN_CHANGED 是否已经确认忘记密码邮件
  private Integer nodeId;// 服务器节点
  private String email; // 用户首要邮件地址
  // 是否生成直接登陆url参数，1已经生成，0未生成。查看sys_user_params
  private Integer urlParams;
  private String mobileNumber; // 手机号

  public Account() {
    super();
  }

  /**
   * 函数. 2018年9月11日
   */
  public Account(Long id, Integer nodeId) {
    super();
    this.id = id;
    this.nodeId = nodeId;
  }

  @Id
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "LOGIN_NAME")
  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "TOKEN_CHANGED")
  public Short getTokenChanged() {
    return this.tokenChanged;
  }

  public void setTokenChanged(Short tokenChanged) {
    this.tokenChanged = tokenChanged;
  }

  @Column(name = "ENABLED")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "URL_PARAMS")
  public Integer getUrlParams() {
    return urlParams;
  }

  public void setUrlParams(Integer urlParams) {
    this.urlParams = urlParams;
  }

  @Column(name = "MOBILE_NUMBER")
  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
}
