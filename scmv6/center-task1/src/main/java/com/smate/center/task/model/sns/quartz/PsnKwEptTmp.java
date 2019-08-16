package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员关键词临时表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PSN_KW_EPT_TMP")
public class PsnKwEptTmp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1855742719970270559L;
  private Long id;
  private Long psnId;
  private String keyWord;
  private String keyWordTxt;
  private Long pt1Num;
  private Long pt2Num;
  private Long pt3Num;
  private Long prjNum;
  private Long kId;
  private Double score;


  public PsnKwEptTmp() {
    super();
  }

  public PsnKwEptTmp(Long id, String keyWord) {
    super();
    this.id = id;
    this.keyWord = keyWord;
  }

  public PsnKwEptTmp(Long id, Long psnId, String keyWord, String keyWordTxt, Long pt1Num, Long pt2Num, Long pt3Num,
      Long prjNum, Long kId, Double score) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.keyWord = keyWord;
    this.keyWordTxt = keyWordTxt;
    this.pt1Num = pt1Num;
    this.pt2Num = pt2Num;
    this.pt3Num = pt3Num;
    this.prjNum = prjNum;
    this.kId = kId;
    this.score = score;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyWord() {
    return keyWord;
  }

  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKeyWordTxt() {
    return keyWordTxt;
  }

  public void setKeyWordTxt(String keyWordTxt) {
    this.keyWordTxt = keyWordTxt;
  }

  @Column(name = "PT1_NUM")
  public Long getPt1Num() {
    return pt1Num;
  }

  public void setPt1Num(Long pt1Num) {
    this.pt1Num = pt1Num;
  }

  @Column(name = "PT2_NUM")
  public Long getPt2Num() {
    return pt2Num;
  }

  public void setPt2Num(Long pt2Num) {
    this.pt2Num = pt2Num;
  }

  @Column(name = "PT3_NUM")
  public Long getPt3Num() {
    return pt3Num;
  }

  public void setPt3Num(Long pt3Num) {
    this.pt3Num = pt3Num;
  }

  @Column(name = "Prj_NUM")
  public Long getPrjNum() {
    return prjNum;
  }

  public void setPrjNum(Long prjNum) {
    this.prjNum = prjNum;
  }

  @Column(name = "KID")
  public Long getkId() {
    return kId;
  }

  public void setkId(Long kId) {
    this.kId = kId;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

}
