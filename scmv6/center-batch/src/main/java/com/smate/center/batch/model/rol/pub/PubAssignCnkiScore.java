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
 * ISI成果匹配-成果指派作者评分.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_CNKISCORE")
public class PubAssignCnkiScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2755308915697615937L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 单位ID
  private Long insId;
  // 用户ID
  private Long psnId;
  // 用户name
  private Float name = 0f;
  // 用户部门
  private Float dept = 0f;
  // 用户关键词
  private Float keywords = 0f;
  // 用户合作者
  private Float coname = 0f;
  // 成果期刊
  private Float journal = 0f;
  // 成果会议名称
  private Float conference = 0f;
  // 成果年度
  private Float pubYear = 0f;
  // 用户单位
  private Float inst = 0f;
  // 用户序号
  private Integer seqNo;
  // 计算总分
  private Float total = 0f;

  public PubAssignCnkiScore() {
    super();
  }

  public PubAssignCnkiScore(Long pubId, Long insId, Long psnId) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.psnId = psnId;
  }

  public PubAssignCnkiScore(PubAssignScoreDetail score) {
    super();
    this.pubId = score.getPubId();
    this.insId = score.getInsId();
    this.psnId = score.getPsnId();
    this.name = score.getName();
    this.dept = score.getDept();
    this.keywords = score.getKeywords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubYear = score.getPubYear();
    this.inst = score.getInst();
    this.seqNo = score.getSeqNo();
    this.total = score.getTotal();
  }

  public void copy(PubAssignCnkiScore score) {
    this.name = score.getName();
    this.dept = score.getDept();
    this.keywords = score.getKeywords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubYear = score.getPubYear();
    this.inst = score.getInst();
    this.seqNo = score.getSeqNo();
    this.total = score.getTotal();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_CNKISCORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NAME")
  public Float getName() {
    return name;
  }

  public void setName(Float name) {
    this.name = name;
  }

  @Column(name = "DEPT")
  public Float getDept() {
    return dept;
  }

  @Column(name = "KEYWORDS")
  public Float getKeywords() {
    return keywords;
  }

  @Column(name = "CONAME")
  public Float getConame() {
    return coname;
  }

  @Column(name = "JOURNAL")
  public Float getJournal() {
    return journal;
  }

  @Column(name = "CONFERENCE")
  public Float getConference() {
    return conference;
  }

  @Column(name = "PUBYEAR")
  public Float getPubYear() {
    return pubYear;
  }

  @Column(name = "INST")
  public Float getInst() {
    return inst;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "TOTAL")
  public Float getTotal() {
    return total;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setDept(Float dept) {
    this.dept = dept;
  }

  public void setKeywords(Float keywords) {
    this.keywords = keywords;
  }

  public void setConame(Float coname) {
    this.coname = coname;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setJournal(Float journal) {
    this.journal = journal;
  }

  public void setConference(Float conference) {
    this.conference = conference;
  }

  public void setPubYear(Float pubYear) {
    this.pubYear = pubYear;
  }

  public void setInst(Float inst) {
    this.inst = inst;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setTotal(Float total) {
    this.total = total;
  }

}
