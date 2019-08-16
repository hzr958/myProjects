package com.smate.core.base.utils.model.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 单位域名设置.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "INS_PORTAL")
public class Sie6InsPortal implements Serializable {

  private static final long serialVersionUID = -3705985102778442522L;

  private String domain;

  private String logo;

  private Long insId;

  private String zhTitle;

  private String defaultLang;

  private String switchLang;

  private String enTitle;

  private String initTitle;

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param domain the domain to set
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @return the logo
   */
  @Column(name = "LOGO")
  public String getLogo() {
    return logo;
  }

  /**
   * @param logo the logo to set
   */
  public void setLogo(String logo) {
    this.logo = logo;
  }

  /**
   * @return the defaultLang
   */
  @Column(name = "DEFAULT_LANG")
  public String getDefaultLang() {
    return defaultLang;
  }

  /**
   * @param defaultLang the defaultLang to set
   */
  public void setDefaultLang(String defaultLang) {
    this.defaultLang = defaultLang;
  }

  /**
   * @return the switchLang
   */
  @Column(name = "SWITCH_LANG")
  public String getSwitchLang() {
    return switchLang;
  }

  /**
   * @param switchLang the switchLang to set
   */
  public void setSwitchLang(String switchLang) {
    this.switchLang = switchLang;
  }

  /**
   * @return the etitle
   */
  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  /**
   * @param etitle the etitle to set
   */
  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @return the etitle
   */
  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  @Transient
  public String getInitTitle() {
    return initTitle;
  }

  public void setInitTitle(String initTitle) {
    this.initTitle = initTitle;
  }
}
