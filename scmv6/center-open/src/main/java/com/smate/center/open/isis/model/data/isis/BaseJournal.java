package com.smate.center.open.isis.model.data.isis;

import javax.persistence.*;
import java.io.Serializable;


/**
 * scmpdwh The persistent class for the BASE_JOURNAL database table.
 * 
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournal implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 3266012274526393739L;

  @Id
  @Column(name = "JNL_ID")
  private Long jnlId;

  @Column(name = "ACTIVE_STATUS")
  private String activeStatus;

  private String cn;

  @Lob
  @Column(name = "DESCRIPTION_EN")
  private String descriptionEn;

  @Lob
  @Column(name = "DESCRIPTION_XX")
  private String descriptionXx;

  @Column(name = "EISSN")
  private String eissn;

  @Column(name = "END_YEAR")
  private String endYear;

  @Column(name = "FREQUENCY_EN")
  private String frequencyEn;

  @Column(name = "FREQUENCY_ZH")
  private String frequencyZh;

  @Column(name = "JOURNAL_URL")
  private String journalUrl;

  @Column(name = "\"LANGUAGE\"")
  private String language;

  @Column(name = "OA_STATUS")
  private String oaStatus;

  @Column(name = "PISSN")
  private String pissn;

  @Column(name = "REGION_ID")
  private Long regionId;

  @Column(name = "ROMEO_COLOUR")
  private String romeoColour;

  @Column(name = "START_YEAR")
  private String startYear;

  @Column(name = "SUBMISSION_URL")
  private String submissionUrl;

  @Column(name = "TITLE_ABBR")
  private String titleAbbr;

  @Column(name = "TITLE_EN")
  private String titleEn;

  @Column(name = "TITLE_XX")
  private String titleXx;

  public BaseJournal() {}

  public long getJnlId() {
    return this.jnlId;
  }

  public void setJnlId(long jnlId) {
    this.jnlId = jnlId;
  }

  public String getActiveStatus() {
    return this.activeStatus;
  }

  public void setActiveStatus(String activeStatus) {
    this.activeStatus = activeStatus;
  }

  public String getCn() {
    return this.cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  public String getDescriptionEn() {
    return this.descriptionEn;
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  public String getDescriptionXx() {
    return this.descriptionXx;
  }

  public void setDescriptionXx(String descriptionXx) {
    this.descriptionXx = descriptionXx;
  }

  public String getEissn() {
    return this.eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

  public String getEndYear() {
    return this.endYear;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  public String getFrequencyEn() {
    return this.frequencyEn;
  }

  public void setFrequencyEn(String frequencyEn) {
    this.frequencyEn = frequencyEn;
  }

  public String getFrequencyZh() {
    return this.frequencyZh;
  }

  public void setFrequencyZh(String frequencyZh) {
    this.frequencyZh = frequencyZh;
  }

  public String getJournalUrl() {
    return this.journalUrl;
  }

  public void setJournalUrl(String journalUrl) {
    this.journalUrl = journalUrl;
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getOaStatus() {
    return this.oaStatus;
  }

  public void setOaStatus(String oaStatus) {
    this.oaStatus = oaStatus;
  }

  public String getPissn() {
    return this.pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  public Long getRegionId() {
    return this.regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getRomeoColour() {
    return this.romeoColour;
  }

  public void setRomeoColour(String romeoColour) {
    this.romeoColour = romeoColour;
  }

  public String getStartYear() {
    return this.startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public String getSubmissionUrl() {
    return this.submissionUrl;
  }

  public void setSubmissionUrl(String submissionUrl) {
    this.submissionUrl = submissionUrl;
  }

  public String getTitleAbbr() {
    return this.titleAbbr;
  }

  public void setTitleAbbr(String titleAbbr) {
    this.titleAbbr = titleAbbr;
  }

  public String getTitleEn() {
    return this.titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getTitleXx() {
    return this.titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

}
