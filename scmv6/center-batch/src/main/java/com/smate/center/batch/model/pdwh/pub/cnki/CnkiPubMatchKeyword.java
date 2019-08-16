package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词匹配表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "CNKI_PUB_MATCH_KEYWORD")
public class CnkiPubMatchKeyword implements Serializable {

  private static final long serialVersionUID = 6743861944365194037L;
  private Long id;// 主键.
  private Long pubId;// 成果ID.
  private Long kwHash;// 关键词Hash.

  public CnkiPubMatchKeyword() {
    super();
  }

  public CnkiPubMatchKeyword(Long pubId, Long kwHash) {
    super();
    this.pubId = pubId;
    this.kwHash = kwHash;
  }

  public CnkiPubMatchKeyword(Long id, Long pubId, Long kwHash) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.kwHash = kwHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_MATCH_KEYWORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "KW_HASH")
  public Long getKwHash() {
    return kwHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setKwHash(Long kwHash) {
    this.kwHash = kwHash;
  }

}
