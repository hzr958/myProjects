package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * scopus成果匹配-成果指派拆分成果关键词名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_SPSKEYWORDS")
public class PubAssignSpsKeywords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8623328252205642919L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 关键词(小写)
  private String keywords;
  // 关键词hash
  private Integer keyHash;
  // 成果所属单位ID
  private Long pubInsId;

  public PubAssignSpsKeywords() {
    super();
  }

  public PubAssignSpsKeywords(Long pubId, String keywords, Integer keyHash, Long pubInsId) {
    super();
    this.pubId = pubId;
    this.keywords = keywords;
    this.keyHash = keyHash;
    this.pubInsId = pubInsId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_SPSKEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "KEYWORDS")
  public String getKeywords() {
    return keywords;
  }

  @Column(name = "KD_HASH")
  public Integer getKeyHash() {
    return keyHash;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public void setKeyHash(Integer keyHash) {
    this.keyHash = keyHash;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }
}
