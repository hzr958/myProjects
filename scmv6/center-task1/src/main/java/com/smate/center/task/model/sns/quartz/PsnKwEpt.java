package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员关键词表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "psn_kw_ept")
public class PsnKwEpt implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5949861257997783385L;
  private Long id;
  // 人员ID
  private Long psnId;
  // 关键词
  private String keyword;
  private String kwTxt;
  // 1sci/ssci成果个数
  private Long pt1Num;
  // 2ei/sps/istp成果个数
  private Long pt2Num;

  // 分组id KeywordsPsnHotKeyword.id
  private Long keywordGid;

  // 3other成果个数
  private Long pt3Num;
  // 在项目中使用的关键词个数
  private Long prjNum;

  // 总分
  private Double score;

  private Double scoreSum;

  public PsnKwEpt() {
    super();
  }

  public PsnKwEpt(Long psnId, Double scoreSum) {
    super();
    this.psnId = psnId;
    this.scoreSum = scoreSum;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KW_EPT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  @Column(name = "PT1_NUM")
  public Long getPt1Num() {
    return pt1Num;
  }

  @Column(name = "PT2_NUM")
  public Long getPt2Num() {
    return pt2Num;
  }

  @Column(name = "PT3_NUM")
  public Long getPt3Num() {
    return pt3Num;
  }

  @Column(name = "PRJ_NUM")
  public Long getPrjNum() {
    return prjNum;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  @Column(name = "gid")
  public Long getKeywordGid() {
    return keywordGid;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public void setKeywordGid(Long keywordGid) {
    this.keywordGid = keywordGid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPt1Num(Long pt1Num) {
    this.pt1Num = pt1Num;
  }

  public void setPt2Num(Long pt2Num) {
    this.pt2Num = pt2Num;
  }

  public void setPt3Num(Long pt3Num) {
    this.pt3Num = pt3Num;
  }

  public void setPrjNum(Long prjNum) {
    this.prjNum = prjNum;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Transient
  public Double getScoreSum() {
    return scoreSum;
  }

  public void setScoreSum(Double scoreSum) {
    this.scoreSum = scoreSum;
  }

}
