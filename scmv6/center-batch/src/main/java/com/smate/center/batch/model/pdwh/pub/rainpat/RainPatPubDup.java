package com.smate.center.batch.model.pdwh.pub.rainpat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LJ
 *
 *         2017年7月17日
 */
@Entity
@Table(name = "RAINPAT_PUB_DUP")
public class RainPatPubDup implements Serializable {
  private static final long serialVersionUID = 9157080183204973113L;
  private Long pubId;
  // PATENT_NO的hash标识
  private Long patentHash;
  // PATENT_OPEN_NO的hash标识
  private Long patentOpenHash;
  // ZH_TITLE|EN_TITLE的hash标识
  private Long titleHash;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "PATENT_HASH")
  public Long getPatentHash() {
    return patentHash;
  }

  public void setPatentHash(Long patentHash) {
    this.patentHash = patentHash;
  }

  @Column(name = "PATENT_OPEN_HASH")
  public Long getPatentOpenHash() {
    return patentOpenHash;
  }

  public void setPatentOpenHash(Long patentOpenHash) {
    this.patentOpenHash = patentOpenHash;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public RainPatPubDup() {
    super();
  }

  public RainPatPubDup(Long pubId, Long patentHash, Long patentOpenHash, Long titleHash) {
    super();
    this.pubId = pubId;
    this.patentHash = patentHash;
    this.patentOpenHash = patentOpenHash;
    this.titleHash = titleHash;
  }



}
