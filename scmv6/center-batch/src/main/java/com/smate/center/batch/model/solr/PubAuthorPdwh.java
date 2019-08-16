package com.smate.center.batch.model.solr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 从pdwh原始xml中拆分出作者
 * 
 */
@Entity
@Table(name = "PUB_AUTHOR_PDWH")
public class PubAuthorPdwh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6498197978836790555L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_PUB_AUTHOR_PDWH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PDWH_PUBALL_ID")
  private Long pubAllId;

  @Column(name = "SEQ_NO")
  private Integer seqNo;

  @Column(name = "PDWH_PUB_ID")
  private Long pubId;

  @Column(name = "")
  private Integer dbId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ABBR_NAME")
  private String abbrName;

  @Column(name = "AUTHOR_POSITION")
  private Integer auPosition;

  public PubAuthorPdwh() {
    super();
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

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAbbrName() {
    return abbrName;
  }

  public void setAbbrName(String abbrName) {
    this.abbrName = abbrName;
  }

  public Integer getAuPosition() {
    return auPosition;
  }

  public void setAuPosition(Integer auPosition) {
    this.auPosition = auPosition;
  }

}
