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

@Entity
@Table(name = "V_OPEN_ERRORLOG")
public class OpenErrorLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7103423366008032149L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_ERRORLOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "OPEN_ID")
  private String openId;

  @Column(name = "TOKEN")
  private String Token;

  @Column(name = "ERROR_DATE")
  private Date errorDate;

  @Column(name = "ERROR_FLAG")
  private String errorFlag;

  @Column(name = "ERROR_INFO")
  private String errorInfo;

  public OpenErrorLog() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    if (openId != null && openId.length() > 100) {
      this.openId = openId.substring(0, 100);
    } else {
      this.openId = openId;
    }
  }

  public String getToken() {
    return Token;
  }

  public void setToken(String token) {
    if (token != null && token.length() > 50) {
      this.Token = token.substring(0, 50);
    } else {
      this.Token = token;
    }
  }

  public Date getErrorDate() {
    return errorDate;
  }

  public void setErrorDate(Date errorDate) {
    this.errorDate = errorDate;
  }

  public String getErrorFlag() {
    return errorFlag;
  }

  public void setErrorFlag(String errorFlag) {
    this.errorFlag = errorFlag;
  }

  public String getErrorInfo() {
    return errorInfo;
  }

  public void setErrorInfo(String errorInfo) {
    this.errorInfo = errorInfo;
  }



}
