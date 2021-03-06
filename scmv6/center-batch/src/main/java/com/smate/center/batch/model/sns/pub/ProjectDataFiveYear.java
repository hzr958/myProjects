package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT_DATA_FIVE_YEAR_ZLL")
// @Table(name = "PROJECT_DATA_2012")
// @Table(name = "PROJECT_DATA_2012_GUANGDONG")
public class ProjectDataFiveYear implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 64923502760041559L;
  private Long id;
  private String applicationCode;
  private String zhKeywords;
  private String enKeywords;
  private String approveCode;
  // private Integer applyYear;
  private String applyYear;
  private String zhTitle;
  private String enTitle;
  private String zhAbstract;
  private String enAbstract;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PROJECT_DATA_FIVE_YEAR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "APPLICATION_CODE")
  public String getApplicationCode() {
    return applicationCode;
  }

  public void setApplicationCode(String applicationCode) {
    this.applicationCode = applicationCode;
  }

  @Column(name = "ZH_KEYWORDS")
  public String getZhKeywords() {
    return zhKeywords;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  @Column(name = "EN_KEYWORDS")
  public String getEnKeywords() {
    return enKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  @Column(name = "APPROVE_CODE")
  public String getApproveCode() {
    return approveCode;
  }

  public void setApproveCode(String approveCode) {
    this.approveCode = approveCode;
  }

  @Column(name = "APPLY_YEAR")
  public String getApplyYear() {
    return applyYear;
  }

  public void setApplyYear(String applyYear) {
    this.applyYear = applyYear;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Column(name = "ZH_ABSTRACT")
  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  @Column(name = "EN_ABSTRACT")
  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public ProjectDataFiveYear() {
    super();
  }

  public ProjectDataFiveYear(Long id, String applicationCode, String zhKeywords, String enKeywords) {
    super();
    this.id = id;
    this.applicationCode = applicationCode;
    this.zhKeywords = zhKeywords;
    this.enKeywords = enKeywords;
  }

  public ProjectDataFiveYear(Long id, String applicationCode, String zhKeywords, String enKeywords,
      String approveCode) {
    super();
    this.id = id;
    this.applicationCode = applicationCode;
    this.zhKeywords = zhKeywords;
    this.enKeywords = enKeywords;
    this.approveCode = approveCode;
  }

  public ProjectDataFiveYear(Long id, String applicationCode, String zhKeywords, String approveCode, String zhTitle,
      String zhAbstract) {
    super();
    this.id = id;
    this.applicationCode = applicationCode;
    this.zhKeywords = zhKeywords;
    this.approveCode = approveCode;
    this.zhTitle = zhTitle;
    this.zhAbstract = zhAbstract;
  }

}
