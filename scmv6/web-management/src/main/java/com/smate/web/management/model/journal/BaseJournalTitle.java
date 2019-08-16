package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.JnlFormateUtils;

/**
 * cwli基础期刊title表.
 */
@Entity
@Table(name = "BASE_JOURNAL_TITLE")
public class BaseJournalTitle implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1617849095895079361L;

  private Long jouTitleId;

  private Long jnlId;

  private Long dbId;

  private String titleXx;

  private String titleEn;

  private String titleAbbr;

  private String pissn;

  private String eissn;

  private String cn;

  private Integer titleStatus;

  private String titleXxAlias;

  private String titleEnAlias;

  private String titleAbbrAlias;

  private String dbCode;

  public BaseJournalTitle() {
    super();
  }

  @Id
  @Column(name = "JOU_TITLE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_TITLE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJouTitleId() {
    return jouTitleId;
  }

  public void setJouTitleId(Long jouTitleId) {
    this.jouTitleId = jouTitleId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "TITLE_ABBR")
  public String getTitleAbbr() {
    return titleAbbr;
  }

  public void setTitleAbbr(String titleAbbr) {
    this.titleAbbr = titleAbbr;
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

  @Column(name = "CN")
  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  @Column(name = "TITLE_STATUS")
  public Integer getTitleStatus() {
    return titleStatus;
  }

  public void setTitleStatus(Integer titleStatus) {
    this.titleStatus = titleStatus;
  }

  @Column(name = "TITLE_XX_ALIAS")
  public String getTitleXxAlias() {
    if (StringUtils.isNotBlank(this.titleXx))
      titleXxAlias = JnlFormateUtils.getStrAlias(this.titleXx);
    return titleXxAlias;
  }

  public void setTitleXxAlias(String titleXxAlias) {
    this.titleXxAlias = titleXxAlias;
  }

  @Column(name = "TITLE_EN_ALIAS")
  public String getTitleEnAlias() {
    if (StringUtils.isNotBlank(this.titleEn))
      titleEnAlias = JnlFormateUtils.getStrAlias(this.titleEn);
    return titleEnAlias;
  }

  public void setTitleEnAlias(String titleEnAlias) {
    this.titleEnAlias = titleEnAlias;
  }

  @Column(name = "TITLE_ABBR_ALIAS")
  public String getTitleAbbrAlias() {
    if (StringUtils.isNotBlank(this.titleAbbr))
      titleAbbrAlias = JnlFormateUtils.getStrAlias(this.titleAbbr);
    return titleAbbrAlias;
  }

  public void setTitleAbbrAlias(String titleAbbrAlias) {
    this.titleAbbrAlias = titleAbbrAlias;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

}
