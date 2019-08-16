package com.smate.center.batch.dao.sns.psn.inforefresh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人特征关键词推荐.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_KW_RMC")
public class PsnKwRmc implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -649221028182492094L;
  private Long id;
  // 人员ID
  private Long psnId;
  // 人员关键词
  private String keyword;
  // 人员关键词文本
  private String keywordTxt;
  // 字个数
  private Integer wordNum;
  // 1：EN，2：ZH
  private Integer type;
  // 是否自填关键词
  private Integer ztTf = 0;
  // 项目关键词TF
  private Integer prjTf = 0;
  // 成果关键词TF
  private Integer pubTf = 0;
  // 成果最小作者序号
  private Integer auSeq = 0;
  // 是否成果通讯作者
  private Integer auPos = 0;
  // 是否核心期刊或者ISI期刊
  private Integer hxj = 0;
  // 第3、通讯作者+自填\项目
  private Integer grade1 = 0;
  // 第3、通讯作者\核心期刊\自填\项目
  private Integer grade2 = 0;
  // 所有关键词
  private Integer grade3 = 0;
  // 得分
  private Double score = 0D;

  // 分组id
  private Long kwGid;

  public PsnKwRmc() {
    super();
  }

  public PsnKwRmc(Long id, Long psnId, String keyword, String keywordTxt) {
    this.id = id;
    this.psnId = psnId;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
  }

  public PsnKwRmc(Long id, Long psnId, String keyword, String keywordTxt, Integer type) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
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
  public String getKeywordTxt() {
    return keywordTxt;
  }

  @Column(name = "WORD_NUM")
  public Integer getWordNum() {
    return wordNum;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  @Column(name = "ZT_TF")
  public Integer getZtTf() {
    return ztTf;
  }

  @Column(name = "PRJ_TF")
  public Integer getPrjTf() {
    return prjTf;
  }

  @Column(name = "PUB_TF")
  public Integer getPubTf() {
    return pubTf;
  }

  @Column(name = "AU_SEQ")
  public Integer getAuSeq() {
    return auSeq;
  }

  @Column(name = "AU_POS")
  public Integer getAuPos() {
    return auPos;
  }

  @Column(name = "HXJ")
  public Integer getHxj() {
    return hxj;
  }

  @Column(name = "GRADE1")
  public Integer getGrade1() {
    return grade1;
  }

  @Column(name = "GRADE2")
  public Integer getGrade2() {
    return grade2;
  }

  @Column(name = "GRADE3")
  public Integer getGrade3() {
    return grade3;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  @Column(name = "KW_GID")
  public Long getKwGid() {
    return kwGid;
  }

  public void setKwGid(Long kwGid) {
    this.kwGid = kwGid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKeywordTxt(String keywordTxt) {
    this.keywordTxt = keywordTxt;
  }

  public void setWordNum(Integer wordNum) {
    this.wordNum = wordNum;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setZtTf(Integer ztTf) {
    this.ztTf = ztTf;
  }

  public void setPrjTf(Integer prjTf) {
    this.prjTf = prjTf;
  }

  public void setPubTf(Integer pubTf) {
    this.pubTf = pubTf;
  }

  public void setAuSeq(Integer auSeq) {
    this.auSeq = auSeq;
  }

  public void setAuPos(Integer auPos) {
    this.auPos = auPos;
  }

  public void setHxj(Integer hxj) {
    this.hxj = hxj;
  }

  public void setGrade1(Integer grade1) {
    this.grade1 = grade1;
  }

  public void setGrade2(Integer grade2) {
    this.grade2 = grade2;
  }

  public void setGrade3(Integer grade3) {
    this.grade3 = grade3;
  }

  public void setScore(Double score) {
    this.score = score;
  }

}
