package com.smate.center.task.single.model.solr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * pdwh库中的论文在sns库中对应的全文文件以及全文图片； 用于论文检索的全文下载与全文图片的显示
 * 
 */
@Entity
@Table(name = "PUB_FULLTEXT_PDWH")
public class PdwhSnsPubFullText implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4527762987815974321L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_PUB_FULLTEXT_PDWH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PDWH_PUBALL_ID")
  private Long pdwhPubAllId;

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;

  @Column(name = "DB_ID")
  private Integer dbId;

  @Column(name = "SNS_PUB_ID")
  private Long snsPubId;

  @Column(name = "SNS_CREATE_PSN_ID")
  private Long snsCreatPsnId;

  public PdwhSnsPubFullText() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubAllId() {
    return pdwhPubAllId;
  }

  public void setPdwhPubAllId(Long pdwhPubAllId) {
    this.pdwhPubAllId = pdwhPubAllId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Long getSnsCreatPsnId() {
    return snsCreatPsnId;
  }

  public void setSnsCreatPsnId(Long snsCreatPsnId) {
    this.snsCreatPsnId = snsCreatPsnId;
  }

}
