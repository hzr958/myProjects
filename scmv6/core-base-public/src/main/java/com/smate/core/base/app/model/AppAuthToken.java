package com.smate.core.base.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * APP用户token
 * 
 * @author LJ
 *
 *         2017年10月25日
 */
@Entity
@Table(name = "APP_AUTH_CODE")
public class AppAuthToken implements Serializable {
  private static final long serialVersionUID = -2599640545663437761L;

  private Long psnId;

  private Date effectivedate;

  private String token;

  public AppAuthToken() {
    super();
  }

  public AppAuthToken(Long psnId, Date effectivedate, String token) {
    super();
    this.psnId = psnId;
    this.effectivedate = effectivedate;
    this.token = token;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "EFFECTIVE_DATE")
  public Date getEffectivedate() {
    return effectivedate;
  }

  public void setEffectivedate(Date effectivedate) {
    this.effectivedate = effectivedate;
  }

  @Column(name = "TOKEN")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
