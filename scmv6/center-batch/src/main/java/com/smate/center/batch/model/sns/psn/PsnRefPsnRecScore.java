package com.smate.center.batch.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 给人员推荐基准文献得分表.
 * 
 * @author lichangwen
 */
@Entity
@Table(name = "PSN_REF_PSNREC_SCORE")
public class PsnRefPsnRecScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7545539059937084319L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_REF_PSNREC_SCORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PUBALL_ID")
  private Long puballId;
  @Column(name = "JNL_ID")
  private Long jnlId;
  @Column(name = "ISSN")
  private String issn;
  // 期刊等级，默认4级[1,2,3,4]
  @Column(name = "GRADE")
  private Integer grade = 4;
  // 相关度:关键词/同义词/翻译词 匹配个数
  @Column(name = "KW_TF")
  private Integer kwTf = 0;
  // 相关度:用户发表期刊次数
  @Column(name = "HT_TF")
  private Integer htTf = 0;
  // 相关度:用户收藏期刊+1
  @Column(name = "GRADE_INNER")
  private Integer gradeInner = 0;
  // 质量:高于等于用户发表论文最多档内+1
  @Column(name = "GRADE_MOST")
  private Integer gradeMost = 0;
  // 质量:用户在期刊上发表过+1
  @Column(name = "GRADE_HT")
  private Integer gradeHt = 0;
  // 合作度:好友在期刊上发表过＝1*N
  @Column(name = "FRD_TF")
  private Integer frdTf = 0;
  // 总分：相关度*Ln(2.72+质量+合作度)
  @Column(name = "SCORE")
  private Double score = 0.0;
  // 文献语言1中文，2英文
  @Column(name = "LANGUAGE")
  private Integer language = 1;

  public PsnRefPsnRecScore() {
    super();
  }

  public PsnRefPsnRecScore(Long puballId, Double score) {
    super();
    this.puballId = puballId;
    this.score = score;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPuballId() {
    return puballId;
  }

  public void setPuballId(Long puballId) {
    this.puballId = puballId;
  }

  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public Integer getKwTf() {
    return kwTf;
  }

  public void setKwTf(Integer kwTf) {
    this.kwTf = kwTf;
  }

  public Integer getHtTf() {
    return htTf;
  }

  public void setHtTf(Integer htTf) {
    this.htTf = htTf;
  }

  public Integer getGradeInner() {
    return gradeInner;
  }

  public void setGradeInner(Integer gradeInner) {
    this.gradeInner = gradeInner;
  }

  public Integer getGradeMost() {
    return gradeMost;
  }

  public void setGradeMost(Integer gradeMost) {
    this.gradeMost = gradeMost;
  }

  public Integer getGradeHt() {
    return gradeHt;
  }

  public void setGradeHt(Integer gradeHt) {
    this.gradeHt = gradeHt;
  }

  public Integer getFrdTf() {
    return frdTf;
  }

  public void setFrdTf(Integer frdTf) {
    this.frdTf = frdTf;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
