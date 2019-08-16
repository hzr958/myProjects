package com.smate.web.v8pub.vo;

public class PubShareVo {
  private Long psnId;// 当前用户PsnId\
  private String receivers;// 接收人 des3PsnId+email
  private String des3PubIds;// 加密的成果id
  private String articleName;// 分享类型
  private Integer resType;// 资源类型
  private Integer dbId;
  private Integer databaseType;
  private String content;
  private String zhTitle;
  private String enTitle;

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getReceivers() {
    return receivers;
  }

  public void setReceivers(String receivers) {
    this.receivers = receivers;
  }

  public String getDes3PubIds() {
    return des3PubIds;
  }

  public void setDes3PubIds(String des3PubIds) {
    this.des3PubIds = des3PubIds;
  }

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public Integer getDbid() {
    return dbId;
  }

  public void setDbid(Integer dbid) {
    this.dbId = dbid;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
