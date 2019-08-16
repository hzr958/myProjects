package com.smate.sie.center.task.model.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TASK_SPLIT_PUB_ORGS")
public class SieTaskSplitPubOrgs implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8406627922962275332L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "BASE_ID")
  private Long BaseId;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "AUTHOR")
  private String author;

  @Column(name = "ZH_TITLE")
  private String zhTitle;

  @Column(name = "AUTHOR_NAMES")
  private String authorNames;

  @Column(name = "PUB_JSON")
  private String pubJson;

  @Column(name = "ORGS")
  private String orgs;


  @Column(name = "EMAIL_AUTHOR")
  private String emailAuthor;

  @Column(name = "STATUS")
  private Integer status;

  public SieTaskSplitPubOrgs() {
    super();
  }



  public SieTaskSplitPubOrgs(Long pubId, Long baseId, String title, String author, String zhTitle, String authorNames,
      String pubJson, String orgs, String emailAuthor, Integer status) {
    super();
    this.pubId = pubId;
    BaseId = baseId;
    this.title = title;
    this.author = author;
    this.zhTitle = zhTitle;
    this.authorNames = authorNames;
    this.pubJson = pubJson;
    this.orgs = orgs;
    this.emailAuthor = emailAuthor;
    this.status = status;
  }



  public Long getPubId() {
    return pubId;
  }


  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }


  public Long getBaseId() {
    return BaseId;
  }


  public void setBaseId(Long baseId) {
    BaseId = baseId;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getAuthor() {
    return author;
  }


  public void setAuthor(String author) {
    this.author = author;
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


  public String getPubJson() {
    return pubJson;
  }


  public void setPubJson(String pubJson) {
    this.pubJson = pubJson;
  }


  public String getOrgs() {
    return orgs;
  }


  public void setOrgs(String orgs) {
    this.orgs = orgs;
  }


  public String getEmailAuthor() {
    return emailAuthor;
  }


  public void setEmailAuthor(String emailAuthor) {
    this.emailAuthor = emailAuthor;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
