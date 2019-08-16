package com.smate.center.task.model.pdwh.pub.oalib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OALIB_PUB_DUP")
public class OalibPubDup {
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  // ZH_TITLE|EN_TITLE的hash标识
  @Column(name = "TITLE_HASH")
  private Long titleHash;
  // Source_id的hash
  @Column(name = "SOURCE_ID_HASH")
  private Long sourceIdHash;
  // "year| original| author"组合，生成的hash标识
  @Column(name = "UNION_HASH")
  private Long unionHash;

  public OalibPubDup() {
    super();
  }

  public OalibPubDup(Long pubId, Long titleHash, Long sourceIdHash, Long unionHash) {
    super();
    this.pubId = pubId;
    this.titleHash = titleHash;
    this.sourceIdHash = sourceIdHash;
    this.unionHash = unionHash;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public Long getSourceIdHash() {
    return sourceIdHash;
  }

  public void setSourceIdHash(Long sourceIdHash) {
    this.sourceIdHash = sourceIdHash;
  }

  public Long getUnionHash() {
    return unionHash;
  }

  public void setUnionHash(Long unionHash) {
    this.unionHash = unionHash;
  }

}
