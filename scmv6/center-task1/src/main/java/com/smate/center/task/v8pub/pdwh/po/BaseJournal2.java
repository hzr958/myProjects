package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊主表.
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournal2 implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8803813149791756689L;
  // 主键
  private Long jouId;
  // 英文刊名
  private String titleEn;
  // 原始刊名
  private String titleXx;
  // Print ISSN
  private String pissn;
  // Electric ISSN
  private String eissn;

  public BaseJournal2() {
    super();
  }

  public BaseJournal2(Long jouId, String titleEn, String titleXx) {

    super();
    this.jouId = jouId;
    this.titleXx = titleXx;
    this.titleEn = titleEn;
  }

  public BaseJournal2(Long jouId, String titleEn, String titleXx, String pissn) {
    super();
    this.jouId = jouId;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
    this.pissn = pissn;
  }

  @Id
  @Column(name = "JNL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJouId() {
    return jouId;
  }

  public void setJouId(Long jouId) {
    this.jouId = jouId;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "EISSN")
  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
