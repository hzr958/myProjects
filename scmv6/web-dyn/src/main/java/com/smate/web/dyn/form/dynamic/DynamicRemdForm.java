package com.smate.web.dyn.form.dynamic;

public class DynamicRemdForm {
  private Long resId; // 资源 Id
  private String des3ResId;// 加密的资源id
  private String resTitle;// 资源标题
  private String resAuthorNames;// 资源作者
  private String resBriefDesc;// 资源简介
  private String fullTextUrl;// 全文图片url
  private String fullTextDownloadUrl;// 全文下载url
  public Integer hasFulltext = 0;// 是否有全文 0=没有 ； 1=有
  public Integer type = 1;// 推荐类型 1论文，2基金

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getResTitle() {
    return resTitle;
  }

  public void setResTitle(String resTitle) {
    this.resTitle = resTitle;
  }

  public String getResAuthorNames() {
    return resAuthorNames;
  }

  public void setResAuthorNames(String resAuthorNames) {
    this.resAuthorNames = resAuthorNames;
  }

  public String getResBriefDesc() {
    return resBriefDesc;
  }

  public void setResBriefDesc(String resBriefDesc) {
    this.resBriefDesc = resBriefDesc;
  }

  public String getFullTextDownloadUrl() {
    return fullTextDownloadUrl;
  }

  public void setFullTextDownloadUrl(String fullTextDownloadUrl) {
    this.fullTextDownloadUrl = fullTextDownloadUrl;
  }

  public String getFullTextUrl() {
    return fullTextUrl;
  }

  public void setFullTextUrl(String fullTextUrl) {
    this.fullTextUrl = fullTextUrl;
  }

  public Integer getHasFulltext() {
    return hasFulltext;
  }

  public void setHasFulltext(Integer hasFulltext) {
    this.hasFulltext = hasFulltext;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
