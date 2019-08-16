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
 * CNKI成果匹配结果表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "CNKI_PUB_MATCH_ASSIGN")
public class CnkiPubMatchAssign implements Serializable {

  private static final long serialVersionUID = -2656631296357745572L;
  private Long id;// 主键.
  private Long pubId;// 成果ID.
  private Long psnId;// 人员ID.
  private Integer name;// 全称匹配上.
  private Integer coName;// 合作者全称匹配个数.
  private Integer journal;// 期刊匹配上.
  private Integer keyword;// 关键词匹配个数.
  private Integer athSeq;// 作者序号.
  private Long athId;// 作者ID.
  private Integer athPos;// 是否通信作者.
  private Integer score;// 匹配分数.
  private Integer status;// 匹配结果：0-待确认；1-已确认；2-拒绝.

  public CnkiPubMatchAssign() {
    super();
  }

  public CnkiPubMatchAssign(Long id, Long pubId, Long psnId, Integer name, Integer coName, Integer journal,
      Integer keyword, Integer athSeq, Long athId, Integer athPos, Integer score, Integer status) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.psnId = psnId;
    this.name = name;
    this.coName = coName;
    this.journal = journal;
    this.keyword = keyword;
    this.athSeq = athSeq;
    this.athId = athId;
    this.athPos = athPos;
    this.score = score;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_MATCH_ASSIGN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NAME")
  public Integer getName() {
    return name;
  }

  @Column(name = "CO_NAME")
  public Integer getCoName() {
    return coName;
  }

  @Column(name = "JOURNAL")
  public Integer getJournal() {
    return journal;
  }

  @Column(name = "KEYWORD")
  public Integer getKeyword() {
    return keyword;
  }

  @Column(name = "ATH_SEQ")
  public Integer getAthSeq() {
    return athSeq;
  }

  @Column(name = "ATH_ID")
  public Long getAthId() {
    return athId;
  }

  @Column(name = "ATH_POS")
  public Integer getAthPos() {
    return athPos;
  }

  @Column(name = "SCORE")
  public Integer getScore() {
    return score;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setName(Integer name) {
    this.name = name;
  }

  public void setCoName(Integer coName) {
    this.coName = coName;
  }

  public void setJournal(Integer journal) {
    this.journal = journal;
  }

  public void setKeyword(Integer keyword) {
    this.keyword = keyword;
  }

  public void setAthSeq(Integer athSeq) {
    this.athSeq = athSeq;
  }

  public void setAthId(Long athId) {
    this.athId = athId;
  }

  public void setAthPos(Integer athPos) {
    this.athPos = athPos;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
