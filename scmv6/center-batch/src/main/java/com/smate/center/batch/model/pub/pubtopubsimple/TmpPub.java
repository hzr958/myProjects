package com.smate.center.batch.model.pub.pubtopubsimple;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_20160406")
public class TmpPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5724719417134493004L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  @Column(name = "PUB_TYPE")
  private Integer pubType;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "PUB_TYPE_NAME")
  private String pubTypeName;
  @Column(name = "CITED_TIMES")
  private Integer citedTimes;
  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;
  @Column(name = "EN_NAME")
  private String enName;
  @Column(name = "ZH_NAME")
  private String zhName;
  @Column(name = "ISSN")
  private String issn;
  @Column(name = "EN_ABSTRACT")
  private String enAbstract;
  @Column(name = "ZH_ABSTRACT")
  private String zhAbstract;
  @Column(name = "KEYWORDS")
  private String keyWords;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
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

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }



}
