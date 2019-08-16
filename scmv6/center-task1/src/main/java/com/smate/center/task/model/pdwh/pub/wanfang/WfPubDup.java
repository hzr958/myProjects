package com.smate.center.task.model.pdwh.pub.wanfang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 万方成果查重表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "WF_PUB_DUP")
public class WfPubDup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3506830996668568306L;
  private Long pubId;
  // "year| original| author"组合，生成的hash标识
  private Long unitHash;
  // ZH_TITLE|EN_TITLE的hash标识
  private Long titleHash;

  public WfPubDup() {
    super();
  }

  public WfPubDup(Long pubId, Long titleHash, Long unitHash) {
    super();
    this.pubId = pubId;
    this.titleHash = titleHash;
    this.unitHash = unitHash;
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
