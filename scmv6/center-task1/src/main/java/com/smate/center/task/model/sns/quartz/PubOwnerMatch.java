package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果作者匹配表，用于确定用户与作者的关系.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_OWNER_MATCH")
public class PubOwnerMatch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3699491994640783390L;
  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // 作者序号（为0表示未匹配上）
  private Integer auSeq;
  // 是否通讯作者
  private Integer auPos;

  public PubOwnerMatch() {
    super();
  }

  public PubOwnerMatch(Long pubId, Long psnId, Integer auSeq, Integer auPos) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.auSeq = auSeq;
    this.auPos = auPos;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "AU_SEQ")
  public Integer getAuSeq() {
    return auSeq;
  }

  @Column(name = "AU_POS")
  public Integer getAuPos() {
    return auPos;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setAuSeq(Integer auSeq) {
    this.auSeq = auSeq;
  }

  public void setAuPos(Integer auPos) {
    this.auPos = auPos;
  }
}
