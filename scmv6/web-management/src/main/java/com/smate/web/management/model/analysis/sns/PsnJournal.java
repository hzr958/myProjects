package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员成果期刊信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "mv_psn_journal")
public class PsnJournal implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -4072583813722320645L;
  private Long id;
  // 成果所有人
  private Long psnId;
  // ISSN
  private String issn;
  // ISSN文本
  private String issnTxt;
  // 期刊等级
  private Integer grade;
  // 是否核心期刊或者ISI期刊
  private Integer hxj;
  // 使用频率
  private Integer tf = 0;

  public PsnJournal() {
    super();
  }

  public PsnJournal(Long psnId, String issn, String issnTxt, Integer grade, Integer hxj) {
    super();
    this.psnId = psnId;
    this.issn = issn;
    this.issnTxt = issnTxt;
    this.grade = grade;
    this.hxj = hxj;
    this.tf = 0;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_JOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "ISSN_TXT")
  public String getIssnTxt() {
    return issnTxt;
  }

  @Column(name = "GRADE")
  public Integer getGrade() {
    return grade;
  }

  @Column(name = "HXJ")
  public Integer getHxj() {
    return hxj;
  }

  @Column(name = "TF")
  public Integer getTf() {
    return tf;
  }

  public void setTf(Integer tf) {
    this.tf = tf;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setIssnTxt(String issnTxt) {
    this.issnTxt = issnTxt;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public void setHxj(Integer hxj) {
    this.hxj = hxj;
  }

}
