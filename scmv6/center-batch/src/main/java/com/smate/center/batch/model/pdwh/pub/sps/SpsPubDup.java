package com.smate.center.batch.model.pdwh.pub.sps;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scopus成果查重表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_PUB_DUP")
public class SpsPubDup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6311965784158901357L;
  private Long pubId;
  // Source_id的hash
  private Long sourceIdHash;
  // "year| original| author"组合，生成的hash标识
  private Long unitHash;
  // ZH_TITLE|EN_TITLE的hash标识
  private Long titleHash;

  public SpsPubDup() {
    super();
  }

  public SpsPubDup(Long pubId, Long sourceIdHash, Long titleHash, Long unitHash) {
    super();
    this.pubId = pubId;
    this.sourceIdHash = sourceIdHash;
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

  @Column(name = "SOURCE_ID_HASH")
  public Long getSourceIdHash() {
    return sourceIdHash;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setSourceIdHash(Long sourceIdHash) {
    this.sourceIdHash = sourceIdHash;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setUnitHash(Long unitHash) {
    this.unitHash = unitHash;
  }

}
