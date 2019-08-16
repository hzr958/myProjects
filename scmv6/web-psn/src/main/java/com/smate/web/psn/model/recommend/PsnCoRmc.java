package com.smate.web.psn.model.recommend;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员合作者推荐.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_CO_RMC")
public class PsnCoRmc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 563889607914744738L;

  // 主键
  private Long id;
  private Long psnId;
  private Long coPsnId;
  // 关键词对上个数
  private Integer kwNum;
  // 部门是否相同
  private Integer deptNum;
  // 申请代码对上个数
  private Integer discNum;
  // 期刊对上个数
  private Integer journalNum;
  // 单位等级是否一样
  private Integer insGrade;
  // 职称等级是否一样
  private Integer positionGrade;
  // 期刊等级是否一样
  private Integer journalGrade;
  // 项目等级是否一样
  private Integer prjGrade;
  // 是否好友
  private Integer isFrd;
  // 共同好友个数
  private Integer ffNum;
  // 质量
  private Double quality;
  // 相关度
  private Double relevancy;
  // 合作度
  private Double cooperation;
  // 推荐度
  private Double recommendation;
  // 是否不再推荐
  private Integer isDel;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CO_PSN_ID")
  public Long getCoPsnId() {
    return coPsnId;
  }

  @Column(name = "KW_NUM")
  public Integer getKwNum() {
    return kwNum;
  }

  @Column(name = "DEPT_NUM")
  public Integer getDeptNum() {
    return deptNum;
  }

  @Column(name = "DISC_NUM")
  public Integer getDiscNum() {
    return discNum;
  }

  @Column(name = "JOURNAL_NUM")
  public Integer getJournalNum() {
    return journalNum;
  }

  @Column(name = "INS_GRADE")
  public Integer getInsGrade() {
    return insGrade;
  }

  @Column(name = "POSITION_GRADE")
  public Integer getPositionGrade() {
    return positionGrade;
  }

  @Column(name = "JOURNAL_GRADE")
  public Integer getJournalGrade() {
    return journalGrade;
  }

  @Column(name = "PRJ_GRADE")
  public Integer getPrjGrade() {
    return prjGrade;
  }

  @Column(name = "IS_FRD")
  public Integer getIsFrd() {
    return isFrd;
  }

  @Column(name = "FF_NUM")
  public Integer getFfNum() {
    return ffNum;
  }

  @Column(name = "QUALITY")
  public Double getQuality() {
    return quality;
  }

  @Column(name = "RELEVANCY")
  public Double getRelevancy() {
    return relevancy;
  }

  @Column(name = "COOPERATION")
  public Double getCooperation() {
    return cooperation;
  }

  @Column(name = "RECOMMENDATION")
  public Double getRecommendation() {
    return recommendation;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCoPsnId(Long coPsnId) {
    this.coPsnId = coPsnId;
  }

  public void setKwNum(Integer kwNum) {
    this.kwNum = kwNum;
  }

  public void setDeptNum(Integer deptNum) {
    this.deptNum = deptNum;
  }

  public void setDiscNum(Integer discNum) {
    this.discNum = discNum;
  }

  public void setJournalNum(Integer journalNum) {
    this.journalNum = journalNum;
  }

  public void setInsGrade(Integer insGrade) {
    this.insGrade = insGrade;
  }

  public void setPositionGrade(Integer positionGrade) {
    this.positionGrade = positionGrade;
  }

  public void setJournalGrade(Integer journalGrade) {
    this.journalGrade = journalGrade;
  }

  public void setPrjGrade(Integer prjGrade) {
    this.prjGrade = prjGrade;
  }

  public void setIsFrd(Integer isFrd) {
    this.isFrd = isFrd;
  }

  public void setFfNum(Integer ffNum) {
    this.ffNum = ffNum;
  }

  public void setQuality(Double quality) {
    this.quality = quality;
  }

  public void setRelevancy(Double relevancy) {
    this.relevancy = relevancy;
  }

  public void setCooperation(Double cooperation) {
    this.cooperation = cooperation;
  }

  public void setRecommendation(Double recommendation) {
    this.recommendation = recommendation;
  }

  @Column(name = "ISDEL")
  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

}
