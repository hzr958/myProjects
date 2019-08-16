package com.smate.center.open.service.data.pub;

public class PubDetail {
  private String pubUrl;
  private String zhTitle;
  private String authorNames;
  private String briefDesc;
  private String pubTypeName;

  public String getPubUrl() {
    return pubUrl;
  }

  public void setPubUrl(String pubUrl) {
    this.pubUrl = pubUrl;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public PubDetail() {
    super();
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public PubDetail(String pubUrl, String zhTitle, String authorNames, String briefDesc, String pubTypeName) {
    super();
    this.pubUrl = pubUrl;
    this.zhTitle = zhTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.pubTypeName = pubTypeName;
  }

}
