package com.smate.web.psn.action.site;

public class InsPortalShow {

  private String domain;

  private String logo;

  private Long insId;

  private String enTitle;

  private String zhTitle;

  private Integer version;// sie机构版本:0:标准版，1：协会版

  private String webCtx; // 上下文路径

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getWebCtx() {
    return webCtx;
  }

  public void setWebCtx(String webCtx) {
    this.webCtx = webCtx;
  }

}
