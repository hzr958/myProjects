package com.smate.center.batch.model.pdwh.pub.cnkipat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CNKI专利成果查重表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKIPAT_PUB_DUP")
public class CnkiPatPubDup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2558330376697530537L;
  private Long pubId;
  // PATENT_NO的hash标识
  private Long patentHash;
  // PATENT_OPEN_NO的hash标识
  private Long patentOpenHash;
  // ZH_TITLE|EN_TITLE的hash标识
  private Long titleHash;

  public CnkiPatPubDup() {
    super();
  }

  public CnkiPatPubDup(Long pubId, Long titleHash, Long patentHash, Long patentOpenHash) {
    super();
    this.pubId = pubId;
    this.patentHash = patentHash;
    this.titleHash = titleHash;
    this.patentOpenHash = patentOpenHash;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PATENT_HASH")
  public Long getPatentHash() {
    return patentHash;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  @Column(name = "PATENT_OPEN_HASH")
  public Long getPatentOpenHash() {
    return patentOpenHash;
  }

  public void setPatentOpenHash(Long patentOpenHash) {
    this.patentOpenHash = patentOpenHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPatentHash(Long patentHash) {
    this.patentHash = patentHash;
  }

}
