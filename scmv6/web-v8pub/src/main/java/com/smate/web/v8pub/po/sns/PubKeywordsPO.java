package com.smate.web.v8pub.po.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词表
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_KEYWORDS")
public class PubKeywordsPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5687447399964726806L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_KEYWORDS_ID", sequenceName = "SEQ_PUB_KEYWORDS_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_KEYWORDS_ID")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "PUB_KEYWORD")
  private String pubKeyword; // 成果关键词 单个

  public PubKeywordsPO() {
    super();
  }

  public PubKeywordsPO(Long pubId, String pubKeyword) {
    super();
    this.pubId = pubId;
    this.pubKeyword = pubKeyword;
  }

  public PubKeywordsPO(Long id, Long pubId, String pubKeyword) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.pubKeyword = pubKeyword;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPubKeyword() {
    return pubKeyword;
  }

  public void setPubKeyword(String pubKeyword) {
    this.pubKeyword = pubKeyword;
  }

}
