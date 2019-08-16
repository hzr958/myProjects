package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 成果匹配评分详情结果.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignScoreDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1407409906031739581L;

  // 用户ID
  private Long psnId;
  private Long insId;
  private Long pubId;
  private Integer seqNo;
  // 用户名
  private Float name = 0f;
  // 用户initName
  private Float initName = 0f;
  // 用户fullName
  private Float fullName = 0f;
  // 用户名称单词匹配上的分数
  private Float nameWord = 0f;
  // 用户email
  private Float email = 0f;
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
  // 计算总分
  private Float total = 0f;

  public PubAssignScoreDetail() {
    super();
  }

  public PubAssignScoreDetail(Long psnId, Long insId, Long pubId, Integer seqNo) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.pubId = pubId;
    this.seqNo = seqNo;
  }

  public PubAssignScoreDetail(Long psnId, Long insId, Long pubId, Integer seqNo, Float email) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.pubId = pubId;
    this.seqNo = seqNo;
    this.email = email;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getPubId() {
    return pubId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Float getName() {
    return name;
  }

  public Float getInitName() {
    return initName;
  }

  public Float getFullName() {
    return fullName;
  }

  public Float getNameWord() {
    return nameWord;
  }

  public Float getEmail() {
    return email;
  }

  public Float getDept() {
    return dept;
  }

  public Float getKeywords() {
    return keywords;
  }

  public Float getConame() {
    return coname;
  }

  public Float getJournal() {
    return journal;
  }

  public Float getConference() {
    return conference;
  }

  public Float getPubYear() {
    return pubYear;
  }

  public Float getInst() {
    return inst;
  }

  // name +initName+fullName+nameWord+email+dept+keywords+coname+journal+conference+pubYear+inst
  public Float getTotal() {
    return total;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setName(Float name) {
    this.name = name;
  }

  public void setInitName(Float initName) {
    this.initName = initName;
  }

  public void setFullName(Float fullName) {
    this.fullName = fullName;
  }

  public void setNameWord(Float nameWord) {
    this.nameWord = nameWord;
  }

  public void setEmail(Float email) {
    this.email = email;
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

  public void setTotal(Float total) {
    this.total = total;
  }

}
