package com.smate.web.v8pub.po.journal;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 基础期刊(标准期刊)
 * 
 * @author TSZ
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournalPO implements Serializable {

  private static final long serialVersionUID = -8672013740483441236L;

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

  public BaseJournalPO() {
    super();
  }

  public BaseJournalPO(Long jouId, String titleEn, String titleXx, String pissn) {
    super();
    this.jouId = jouId;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
    this.pissn = pissn;

  }

  @Id
  @Column(name = "JNL_ID")
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
