package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果关键词表
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_PDWH_KEYWORDS")
public class PdwhPubKeywordsPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3335166275595784715L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_KEYWOREDS_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

  @Column(name = "PUB_KEYWORD")
  private String pubKeyword; // 成果关键词 单个

  public PdwhPubKeywordsPO() {
    super();
  }

  public PdwhPubKeywordsPO(Long pdwhPubId, String pubKeyword) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.pubKeyword = pubKeyword;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getPubKeyword() {
    return pubKeyword;
  }

  public void setPubKeyword(String pubKeyword) {
    this.pubKeyword = pubKeyword;
  }

}
