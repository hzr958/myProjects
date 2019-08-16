package com.smate.web.management.model.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

public class PubInfoForm implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long pubId;
  private String des3PubId = "";// 成果id
  private Integer pubType;// 成果类型
  private String title = "";// 标题
  private String summary = ""; // 摘要，概要
  private String briefDesc = ""; // 简短描述
  private String keywords = "";// 关键词数组
  private Integer citations; // 引用次数
  private String authorNames = "";
  private Date gmtModified; // 成果更新时间
  private Date gmtCreate; // 成果添加时间
  private List<PubPdwhPO> pubInfoList;
  private Page<PubPdwhPO> page;
  private PubPdwhPO pubPdwhPO;
  private String des3PubIds = "";

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public List<PubPdwhPO> getPubInfoList() {
    return pubInfoList;
  }

  public void setPubInfoList(List<PubPdwhPO> pubInfoList) {
    this.pubInfoList = pubInfoList;
  }

  public Page<PubPdwhPO> getPage() {
    return page;
  }

  public void setPage(Page<PubPdwhPO> page) {
    this.page = page;
  }

  public PubPdwhPO getPubPdwhPO() {
    if (pubPdwhPO == null) {
      pubPdwhPO = new PubPdwhPO();
      pubPdwhPO.setPubId(pubId);
      pubPdwhPO.setTitle(title);
      pubPdwhPO.setAuthorNames(authorNames);
      pubPdwhPO.setBriefDesc(briefDesc);
      pubPdwhPO.setGmtCreate(gmtCreate);
      pubPdwhPO.setGmtModified(gmtModified);
    }
    return pubPdwhPO;
  }

  public void setPubPdwhPO(PubPdwhPO pubPdwhPO) {
    this.pubPdwhPO = pubPdwhPO;
  }

  public String getDes3PubIds() {
    return des3PubIds;
  }

  public void setDes3PubIds(String des3PubIds) {
    this.des3PubIds = des3PubIds;
  }

}
