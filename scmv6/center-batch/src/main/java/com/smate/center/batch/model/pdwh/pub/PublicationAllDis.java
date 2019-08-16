package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 文献-领域对应表
 * 
 * @author warrior
 * 
 */
@Entity
@Table(name = "PUBLICATION_ALL_DIS")
public class PublicationAllDis implements Serializable {

  private static final long serialVersionUID = -6230394129816373618L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ALL_DIS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  // 成果ID
  @Column(name = "PUB_ALL_ID")
  private Long pubAllId;
  // 研究领域ID
  @Column(name = "DIS_ID")
  private Long disId;

  public PublicationAllDis() {
    super();
  }

  public PublicationAllDis(Long pubAllId, Long disId) {
    this.pubAllId = pubAllId;
    this.disId = disId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

}
