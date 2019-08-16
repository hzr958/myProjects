package com.smate.center.batch.model.sns.isisstat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ISIS_RPT_PUB_STAT")
public class IsisRptPubStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4012446526352541518L;

  @Id
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "INS_NAME")
  private String insName;
  @Column(name = "PRT_YEAR")
  private Long rptYear;
  @Column(name = "AWARD_NAT_ZRKX1")
  private Long awardNatZrkx1;
  @Column(name = "AWARD_NAT_ZRKX2")
  private Long awardNatZrkx2;
  @Column(name = "AWARD_NAT_KJJB1")
  private Long awardNatkjjb1;
  @Column(name = "AWARD_NAT_KJJB2")
  private Long awardNatkjjb2;
  @Column(name = "AWARD_NAT_FM1")
  private Long awardNatfm1;
  @Column(name = "AWARD_NAT_FM2")
  private Long awardNatfm2;
  @Column(name = "AWARD_PRV_ZRKX1")
  private Long awardPrvZrkx1;
  @Column(name = "AWARD_PRV_ZRKX2")
  private Long awardPrvZrkx2;
  @Column(name = "AWARD_PRV_KJJB1")
  private Long awardPrvKjjb1;
  @Column(name = "AWARD_PRV_KJJB2")
  private Long awardPrvKjjb2;
  @Column(name = "AWARD_INT_XS")
  private Long awardIntXs;
  @Column(name = "AWARD_OTHER")
  private Long awardOther;
  @Column(name = "REPORT_INT_TY")
  private Long reportIntTy;
  @Column(name = "REPORT_INT_TZ")
  private Long reportIntFz;
  @Column(name = "REPORT_NAT_TY")
  private Long reportNatTy;
  @Column(name = "REPORT_NAT_FZ")
  private Long reportNatFz;
  @Column(name = "JOURNAL_HOME_YB")
  private Long journalHomeYb;
  @Column(name = "JOURNAL_HOME_HX")
  private Long journalHomeHx;
  @Column(name = "JOURNAL_INT")
  private Long journalInt;
  @Column(name = "JOURNAL_IDX_EI")
  private Long journalIdxEi;
  @Column(name = "JOURNAL_INX_ISTP")
  private Long journalIdxIstp;
  @Column(name = "JOURNAL_INX_SCI")
  private Long journalIdxSci;
  @Column(name = "JOURNAL_INX_ISR")
  private Long journalIdxIsr;
  @Column(name = "ZH_YCB")
  private Long ZhYcb;
  @Column(name = "ZH_DCB")
  private Long ZhDcb;
  @Column(name = "EN_YCB")
  private Long EnYcb;
  @Column(name = "EN_DCB")
  private Long EnDcb;
  @Column(name = "HOME_APP")
  private Long HomeApp;
  @Column(name = "HOME_AUTH")
  private Long HomeAuth;
  @Column(name = "ABROAD_APP")
  private Long AbroadApp;
  @Column(name = "ABROAD_AUTH")
  private Long AbroadAuth;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getRptYear() {
    return rptYear;
  }

  public void setRptYear(Long rptYear) {
    this.rptYear = rptYear;
  }

  public Long getAwardNatZrkx1() {
    return awardNatZrkx1;
  }

  public void setAwardNatZrkx1(Long awardNatZrkx1) {
    this.awardNatZrkx1 = awardNatZrkx1;
  }

  public Long getAwardNatZrkx2() {
    return awardNatZrkx2;
  }

  public void setAwardNatZrkx2(Long awardNatZrkx2) {
    this.awardNatZrkx2 = awardNatZrkx2;
  }

  public Long getAwardNatkjjb1() {
    return awardNatkjjb1;
  }

  public void setAwardNatkjjb1(Long awardNatkjjb1) {
    this.awardNatkjjb1 = awardNatkjjb1;
  }

  public Long getAwardNatkjjb2() {
    return awardNatkjjb2;
  }

  public void setAwardNatkjjb2(Long awardNatkjjb2) {
    this.awardNatkjjb2 = awardNatkjjb2;
  }

  public Long getAwardNatfm1() {
    return awardNatfm1;
  }

  public void setAwardNatfm1(Long awardNatfm1) {
    this.awardNatfm1 = awardNatfm1;
  }

  public Long getAwardNatfm2() {
    return awardNatfm2;
  }

  public void setAwardNatfm2(Long awardNatfm2) {
    this.awardNatfm2 = awardNatfm2;
  }

  public Long getAwardPrvZrkx1() {
    return awardPrvZrkx1;
  }

  public void setAwardPrvZrkx1(Long awardPrvZrkx1) {
    this.awardPrvZrkx1 = awardPrvZrkx1;
  }

  public Long getAwardPrvZrkx2() {
    return awardPrvZrkx2;
  }

  public void setAwardPrvZrkx2(Long awardPrvZrkx2) {
    this.awardPrvZrkx2 = awardPrvZrkx2;
  }

  public Long getAwardPrvKjjb1() {
    return awardPrvKjjb1;
  }

  public void setAwardPrvKjjb1(Long awardPrvKjjb1) {
    this.awardPrvKjjb1 = awardPrvKjjb1;
  }

  public Long getAwardPrvKjjb2() {
    return awardPrvKjjb2;
  }

  public void setAwardPrvKjjb2(Long awardPrvKjjb2) {
    this.awardPrvKjjb2 = awardPrvKjjb2;
  }

  public Long getAwardIntXs() {
    return awardIntXs;
  }

  public void setAwardIntXs(Long awardIntXs) {
    this.awardIntXs = awardIntXs;
  }

  public Long getAwardOther() {
    return awardOther;
  }

  public void setAwardOther(Long awardOther) {
    this.awardOther = awardOther;
  }

  public Long getReportIntTy() {
    return reportIntTy;
  }

  public void setReportIntTy(Long reportIntTy) {
    this.reportIntTy = reportIntTy;
  }

  public Long getReportIntFz() {
    return reportIntFz;
  }

  public void setReportIntFz(Long reportIntFz) {
    this.reportIntFz = reportIntFz;
  }

  public Long getReportNatTy() {
    return reportNatTy;
  }

  public void setReportNatTy(Long reportNatTy) {
    this.reportNatTy = reportNatTy;
  }

  public Long getReportNatFz() {
    return reportNatFz;
  }

  public void setReportNatFz(Long reportNatFz) {
    this.reportNatFz = reportNatFz;
  }

  public Long getJournalHomeYb() {
    return journalHomeYb;
  }

  public void setJournalHomeYb(Long journalHomeYb) {
    this.journalHomeYb = journalHomeYb;
  }

  public Long getJournalHomeHx() {
    return journalHomeHx;
  }

  public void setJournalHomeHx(Long journalHomeHx) {
    this.journalHomeHx = journalHomeHx;
  }

  public Long getJournalInt() {
    return journalInt;
  }

  public void setJournalInt(Long journalInt) {
    this.journalInt = journalInt;
  }

  public Long getJournalIdxEi() {
    return journalIdxEi;
  }

  public void setJournalIdxEi(Long journalIdxEi) {
    this.journalIdxEi = journalIdxEi;
  }

  public Long getJournalIdxIstp() {
    return journalIdxIstp;
  }

  public void setJournalIdxIstp(Long journalIdxIstp) {
    this.journalIdxIstp = journalIdxIstp;
  }

  public Long getJournalIdxSci() {
    return journalIdxSci;
  }

  public void setJournalIdxSci(Long journalIdxSci) {
    this.journalIdxSci = journalIdxSci;
  }

  public Long getJournalIdxIsr() {
    return journalIdxIsr;
  }

  public void setJournalIdxIsr(Long journalIdxIsr) {
    this.journalIdxIsr = journalIdxIsr;
  }

  public Long getZhYcb() {
    return ZhYcb;
  }

  public void setZhYcb(Long zhYcb) {
    ZhYcb = zhYcb;
  }

  public Long getZhDcb() {
    return ZhDcb;
  }

  public void setZhDcb(Long zhDcb) {
    ZhDcb = zhDcb;
  }

  public Long getEnYcb() {
    return EnYcb;
  }

  public void setEnYcb(Long enYcb) {
    EnYcb = enYcb;
  }

  public Long getEnDcb() {
    return EnDcb;
  }

  public void setEnDcb(Long enDcb) {
    EnDcb = enDcb;
  }

  public Long getHomeApp() {
    return HomeApp;
  }

  public void setHomeApp(Long homeApp) {
    HomeApp = homeApp;
  }

  public Long getHomeAuth() {
    return HomeAuth;
  }

  public void setHomeAuth(Long homeAuth) {
    HomeAuth = homeAuth;
  }

  public Long getAbroadApp() {
    return AbroadApp;
  }

  public void setAbroadApp(Long abroadApp) {
    AbroadApp = abroadApp;
  }

  public Long getAbroadAuth() {
    return AbroadAuth;
  }

  public void setAbroadAuth(Long abroadAuth) {
    AbroadAuth = abroadAuth;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
