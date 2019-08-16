package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pub_info_tmp_zll_20170801")
public class PubInfoTmp implements Serializable {

  private static final long serialVersionUID = 6101903316073357048L;
  private Long pubId;
  private Long owerPsnId;
  private String patentType;
  private String patentPublishDate;
  private String startDate;
  private String endDate;
  private String orignal;
  private String startPage;
  private String endPage;
  private String volume;
  private String issue;
  private String publishDate;
  private String paperType;
  private Long zhTitleHash;
  private Long enTitleHash;
  private String pubUrl;
  private String psnUrl;
  private String patentOrg;

  public PubInfoTmp() {
    super();
  }

  public PubInfoTmp(Long pubId, Long owerPsnId, String patentType, String patentPublishDate, String startDate,
      String endDate, String orignal, String startPage, String endPage, String volume, String issue, String publishDate,
      String paperType) {
    super();
    this.pubId = pubId;
    this.owerPsnId = owerPsnId;
    this.patentType = patentType;
    this.paperType = paperType;
    this.patentPublishDate = patentPublishDate;
    this.startDate = startDate;
    this.endDate = endDate;
    this.orignal = orignal;
    this.startPage = startPage;
    this.endPage = endPage;
    this.volume = volume;
    this.issue = issue;
    this.publishDate = publishDate;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "OWER_PSN_ID")
  public Long getOwerPsnId() {
    return owerPsnId;
  }

  public void setOwerPsnId(Long owerPsnId) {
    this.owerPsnId = owerPsnId;
  }

  @Column(name = "PATENT_TYPE")
  public String getPatentType() {
    return patentType;
  }

  public void setPatentType(String patentType) {
    this.patentType = patentType;
  }

  @Column(name = "PATENT_PUBLISH_DATE")
  public String getPatentPublishDate() {
    return patentPublishDate;
  }

  public void setPatentPublishDate(String patentPublishDate) {
    this.patentPublishDate = patentPublishDate;
  }

  @Column(name = "START_DATE")
  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  @Column(name = "END_DATE")
  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  @Column(name = "ORIGNAL")
  public String getOrignal() {
    return orignal;
  }

  public void setOrignal(String orignal) {
    this.orignal = orignal;
  }

  @Column(name = "START_PAGE")
  public String getStartPage() {
    return startPage;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  @Column(name = "END_PAGE")
  public String getEndPage() {
    return endPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  @Column(name = "PUBLISH_DATE")
  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  @Column(name = "PAPER_TYPE")
  public String getPaperType() {
    return paperType;
  }

  public void setPaperType(String paperType) {
    this.paperType = paperType;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Long getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Long zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Long getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Long enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  @Column(name = "PUB_URL")
  public String getPubUrl() {
    return pubUrl;
  }

  public void setPubUrl(String pubUrl) {
    this.pubUrl = pubUrl;
  }

  @Column(name = "PSN_URL")
  public String getPsnUrl() {
    return psnUrl;
  }

  public void setPsnUrl(String psnUrl) {
    this.psnUrl = psnUrl;
  }

  @Column(name = "patent_org")
  public String getPatentOrg() {
    return patentOrg;
  }

  public void setPatentOrg(String patentOrg) {
    this.patentOrg = patentOrg;
  }



}
