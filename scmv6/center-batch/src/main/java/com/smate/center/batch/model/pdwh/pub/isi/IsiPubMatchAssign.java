package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ISI成果匹配结果表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "ISI_PUB_MATCH_ASSIGN")
public class IsiPubMatchAssign implements Serializable {

  private static final long serialVersionUID = -2534086590333259756L;
  private Long assignId;// 地址ID.
  private Long pubId;// 成果ID.
  private Long psnId;// 人员ID.
  private Integer iName;// 简称匹配上.
  private Integer fName;// 全称匹配上.
  private Integer email;// email匹配上.
  private Integer coEmail;// 合作者邮件匹配个数.
  private Integer coIName;// 合作者简称匹配个数.
  private Integer coFName;// 合作者全称匹配个数.
  private Integer journal;// 期刊匹配上.
  private Integer keyword;// 关键词匹配个数.
  private Integer athSeq;// 作者序号.
  private Long athId;// 作者ID.
  private Integer athPos;// 是否通信作者.
  private Integer score;// 匹配分数.
  private Integer status;// 匹配状态：0-待确认；1-已确认；2-拒绝.

  public IsiPubMatchAssign() {
    super();
  }

  public IsiPubMatchAssign(Long assignId, Long pubId, Long psnId, Integer iName, Integer fName, Integer email,
      Integer coEmail, Integer coIName, Integer coFName, Integer journal, Integer keyword, Integer athSeq, Long athId,
      Integer athPos, Integer score, Integer status) {
    super();
    this.assignId = assignId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.iName = iName;
    this.fName = fName;
    this.email = email;
    this.coEmail = coEmail;
    this.coIName = coIName;
    this.coFName = coFName;
    this.journal = journal;
    this.keyword = keyword;
    this.athSeq = athSeq;
    this.athId = athId;
    this.athPos = athPos;
    this.score = score;
    this.status = status;
  }

  @Id
  @Column(name = "ASSIGN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_MATCH_ASSIGN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getAssignId() {
    return assignId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INAME")
  public Integer getiName() {
    return iName;
  }

  @Column(name = "FNAME")
  public Integer getfName() {
    return fName;
  }

  @Column(name = "EMAIL")
  public Integer getEmail() {
    return email;
  }

  @Column(name = "CO_EMAIL")
  public Integer getCoEmail() {
    return coEmail;
  }

  @Column(name = "CO_INAME")
  public Integer getCoIName() {
    return coIName;
  }

  @Column(name = "CO_FNAME")
  public Integer getCoFName() {
    return coFName;
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

  public void setAssignId(Long assignId) {
    this.assignId = assignId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setiName(Integer iName) {
    this.iName = iName;
  }

  public void setfName(Integer fName) {
    this.fName = fName;
  }

  public void setEmail(Integer email) {
    this.email = email;
  }

  public void setCoEmail(Integer coEmail) {
    this.coEmail = coEmail;
  }

  public void setCoIName(Integer coIName) {
    this.coIName = coIName;
  }

  public void setCoFName(Integer coFName) {
    this.coFName = coFName;
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
