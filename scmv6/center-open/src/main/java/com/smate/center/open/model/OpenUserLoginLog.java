package com.smate.center.open.model;


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
 * 人员关联sns登录信息表
 */
@Entity
@Table(name = "V_OPEN_USER_LOGINLOG")
public class OpenUserLoginLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6308119486719209804L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_USER_LOGINLOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSNID")
  private Long psnId; // 关联sns人员id
  @Column(name = "LOGIN_DATE")
  private Date loginDate; // 登录时间
  @Column(name = "LOGIN_FROM_TOKEN")
  private String loginFromToken;// 登录来源token
  @Column(name = "LOGIN_MSG")
  private String loginMsg; // 登录信息

  public OpenUserLoginLog() {
    super();
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getLoginDate() {
    return loginDate;
  }

  public void setLoginDate(Date loginDate) {
    this.loginDate = loginDate;
  }

  public String getLoginFromToken() {
    return loginFromToken;
  }

  public void setLoginFromToken(String loginFromToken) {
    this.loginFromToken = loginFromToken;
  }

  public String getLoginMsg() {
    return loginMsg;
  }

  public void setLoginMsg(String loginMsg) {
    this.loginMsg = loginMsg;
  }

}
