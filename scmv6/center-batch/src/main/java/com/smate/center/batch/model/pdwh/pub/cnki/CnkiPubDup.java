package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * cnki成果查重表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_PUB_DUP")
public class CnkiPubDup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7419277839626610048L;

  private Long pubId;
  // "year| original| author"组合，生成的hash标识
  private Long unitHash;
  // ZH_TITLE|EN_TITLE的hash标识
  private Long titleHash;

  public CnkiPubDup() {
    super();
  }

  public CnkiPubDup(Long pubId, Long titleHash, Long unitHash) {
    super();
    this.pubId = pubId;
    this.unitHash = unitHash;
    this.titleHash = titleHash;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "UNIT_HASH")
  public Long getUnitHash() {
    return unitHash;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setUnitHash(Long unitHash) {
    this.unitHash = unitHash;
  }

}
