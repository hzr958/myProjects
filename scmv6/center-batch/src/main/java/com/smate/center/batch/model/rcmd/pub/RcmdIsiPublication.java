package com.smate.center.batch.model.rcmd.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库ISI成果.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "ISI_PUBLICATION")
public class RcmdIsiPublication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7436928422436261441L;

  private Long pubId;
  private String title;
  private Long titleHash;
  private Integer pubType;
  private String sourceId;
  private Long issnHash;
  private Long confnHash;

  public RcmdIsiPublication() {}

  public RcmdIsiPublication(Long pubId) {
    this.pubId = pubId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "SOURCE_ID")
  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  @Column(name = "ISSN_HASH")
  public Long getIssnHash() {
    return issnHash;
  }

  public void setIssnHash(Long issnHash) {
    this.issnHash = issnHash;
  }

  @Column(name = "CONFN_HASH")
  public Long getConfnHash() {
    return confnHash;
  }

  public void setConfnHash(Long confnHash) {
    this.confnHash = confnHash;
  }



}
