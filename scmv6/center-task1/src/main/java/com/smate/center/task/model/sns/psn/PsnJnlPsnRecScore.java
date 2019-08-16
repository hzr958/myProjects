package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cwli期刊推荐，按人员推荐期刊得分表.
 */
@Entity
@Table(name = "PSN_JNL_PSNREC_SCORE")
public class PsnJnlPsnRecScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4287855070928640363L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_JNL_PSNREC_SCORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "JNL_ID")
  private Long jnlId;
  @Column(name = "ISSN")
  private String issn;
  // 期刊等级，默认4级[1,2,3,4]
  @Column(name = "GRADE")
  private Integer grade = 4;
  // 相关度:关键词匹配个数
  @Column(name = "KW_TF")
  private Integer kwTf = 0;
  // 相关度:用户发表期刊次数
  @Column(name = "HT_TF")
  private Integer htTf = 0;
  // 相关度:用户收藏期刊
  @Column(name = "GRADE_INNER")
  private Integer gradeInner = 0;
  // 质量:在用户发表论文最多档内+1
  @Column(name = "GRADE_MOST")
  private Integer gradeMost = 0;
  // 质量:用户在期刊上发表过+1
  @Column(name = "GRADE_HT")
  private Integer gradeHt = 0;
  // 合作度:好友在期刊上发表过＝1
  @Column(name = "FRD_TF")
  private Integer frdTf = 0;
  // 总分：(1+相关度 +质量)＊Ln(2.72+合作度) [新算法］
  @Column(name = "SCORE")
  private Double score = 0.0;
  // 推荐类型：1个人关键词，2发表期刊，3(参考文献中的期刊)阅读期刊，4合作者发表的期刊相同
  @Column(name = "TYPE")
  private Integer type;

  public PsnJnlPsnRecScore() {
    super();
  }

  public PsnJnlPsnRecScore(Long jnlId, Double score) {
    super();
    this.jnlId = jnlId;
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

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
