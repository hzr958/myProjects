package com.smate.core.base.utils.model.local;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * 保存用户切换语言版本.
 * 
 * @author weilongpeng
 * 
 */
@Entity
@Table(name = "SYS_USER_LOCALE")
public class SysUserLocale implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -5034648943743785323L;
  /** 主健. */
  private Long psnId;
  /** 语言. */
  private String locale;

  /** 用户是否登录且是否设置语言版本. */
  private int status = 0;

  public SysUserLocale() {};

  public SysUserLocale(Long psnId, String locale) {
    this.psnId = psnId;
    this.locale = locale;
  }

  public SysUserLocale(Long psnId, String locale, int status) {
    this.psnId = psnId;
    this.locale = locale;
    this.status = status;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "LOCALE")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  @Transient
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
