package com.smate.web.mobile.v8pub.vo.pubfulltext;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果全文匹配到的推荐人员成果表.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "PUB_FULLTEXT_PSN_RCMD")
public class PubFulltextPsnRcmd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7712101371096566204L;

  private Long id;
  /** 对应ftrcmd2.PUB_FULLTEXT_PSN_RCMD的id字段. */
  private Long rcmdId;
  /** 个人成果库成果. */
  private Long pubId;
  /** 成果拥有者. */
  private Long psnId;
  private Long fulltextFileId;
  /** 哪里来的全文：0个人库，1ISI库. */
  private Integer dbId;
  /** 匹配类型：1、sourceId，2、title. */
  private Integer matchType;
  /** 确认状态：0未确认，1确认是我的成果全文，2确认不是我的成果全文. */
  private Integer status;
  /** 推荐时间. */
  private Date rcmdDate;
  /** 来源成果. */
  private Long srcPubId;
  /** 来源人员. */
  private Long srcPsnId;

  public PubFulltextPsnRcmd() {}

  public PubFulltextPsnRcmd(Long rcmdId, Long pubId, Long psnId, Long fulltextFileId, Integer dbId, Integer matchType,
      Integer status, Date rcmdDate, Long srcPubId, Long srcPsnId) {
    this.rcmdId = rcmdId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.fulltextFileId = fulltextFileId;
    this.dbId = dbId;
    this.matchType = matchType;
    this.status = status;
    this.rcmdDate = rcmdDate;
    this.srcPubId = srcPubId;
    this.srcPsnId = srcPsnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_FULLTEXT_PSN_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "RCMD_ID")
  public Long getRcmdId() {
    return rcmdId;
  }

  public void setRcmdId(Long rcmdId) {
    this.rcmdId = rcmdId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FULLTEXT_FILE_ID")
  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "MATCH_TYPE")
  public Integer getMatchType() {
    return matchType;
  }

  public void setMatchType(Integer matchType) {
    this.matchType = matchType;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "RCMD_DATE")
  public Date getRcmdDate() {
    return rcmdDate;
  }

  public void setRcmdDate(Date rcmdDate) {
    this.rcmdDate = rcmdDate;
  }

  @Column(name = "SRC_PUB_ID")
  public Long getSrcPubId() {
    return srcPubId;
  }

  public void setSrcPubId(Long srcPubId) {
    this.srcPubId = srcPubId;
  }

  @Column(name = "SRC_PSN_ID")
  public Long getSrcPsnId() {
    return srcPsnId;
  }

  public void setSrcPsnId(Long srcPsnId) {
    this.srcPsnId = srcPsnId;
  }

}
