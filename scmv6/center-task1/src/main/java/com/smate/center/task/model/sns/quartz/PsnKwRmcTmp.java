package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "psn_kw_rmc_tmp")
public class PsnKwRmcTmp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1467356310874837774L;

  private Long id;
  private Long psnId;
  private String keyWord;
  private String keyWordTxt;
  private int wordNum;
  private Integer type;// 1.en,2.zh
  private int ztTf;// 是否自填关键词
  private Integer prjTf;// 项目自填关键词
  private Integer pubTf;// 成果自填关键词
  private int auSeq;// 成果最小作者序号
  private int auPos;// 是否成果通讯作者
  private int hxj;// 是否核心齐康或者ISI期刊
  private Integer grade1;// 第3、通讯作者+自填\项目
  private Integer grade2;// 第3、通讯作者\核心期刊\自填\项目
  private Integer grade3;// 所有关键词
  private Double score;// 得分

  public PsnKwRmcTmp() {
    super();
  }

  public PsnKwRmcTmp(Long id, String keyWordTxt) {
    super();
    this.id = id;
    this.keyWordTxt = keyWordTxt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KW_RMC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Column(name = "WORD_NUM")
  public int getWordNum() {
    return wordNum;
  }

  public void setWordNum(int i) {
    this.wordNum = i;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "ZT_TF")
  public int getZtTf() {
    return ztTf;
  }

  public void setZtTf(int i) {
    this.ztTf = i;
  }

  @Column(name = "PRJ_TF")
  public Integer getPrjTf() {
    return prjTf;
  }

  public void setPrjTf(Integer prjTf) {
    this.prjTf = prjTf;
  }

  @Column(name = "AU_SEQ")
  public int getAuSeq() {
    return auSeq;
  }

  public void setAuSeq(int auSeq) {
    this.auSeq = auSeq;
  }

  @Column(name = "AU_POS")
  public int getAuPos() {
    return auPos;
  }

  public void setAuPos(int auPos) {
    this.auPos = auPos;
  }

  @Column(name = "HXJ")
  public int getHxj() {
    return hxj;
  }

  public void setHxj(int hxj) {
    this.hxj = hxj;
  }

  @Column(name = "GRADE1")
  public Integer getGrade1() {
    return grade1;
  }

  public void setGrade1(Integer grade1) {
    this.grade1 = grade1;
  }

  @Column(name = "GRADE2")
  public Integer getGrade2() {
    return grade2;
  }

  public void setGrade2(Integer grade2) {
    this.grade2 = grade2;
  }

  @Column(name = "GRADE3")
  public Integer getGrade3() {
    return grade3;
  }

  public void setGrade3(Integer grade3) {
    this.grade3 = grade3;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Column(name = "PUB_TF")
  public Integer getPubTf() {
    return pubTf;
  }

  public void setPubTf(Integer pubTf) {
    this.pubTf = pubTf;
  }
}
