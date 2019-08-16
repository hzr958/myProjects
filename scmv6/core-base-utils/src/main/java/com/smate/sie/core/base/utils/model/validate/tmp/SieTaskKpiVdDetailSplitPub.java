package com.smate.sie.core.base.utils.model.validate.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_KPI_VDDETAIL_SPLIT_PUB")
public class SieTaskKpiVdDetailSplitPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4121880024856978927L;

  @Id
  @Column(name = "DETAIL_ID")
  private Long detailId;

  @Column(name = "UUID")
  private String uuId;

  @Column(name = "PNAMES")
  private String pNames;

  @Column(name = "YEAR")
  private Integer year;

  @Column(name = "JNAME")
  private String jName;

  @Column(name = "AUTHOR")
  private String author;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DOI")
  private String doi;

  @Column(name = "FUNDINFO")
  private String fundInfo;

  @Column(name = "V_FUNDINGINFO")
  private Integer vFundingInfo;

  @Column(name = "C_FUNDINGINFO")
  private String cFundingInfo;

  @Column(name = "V_YEAR")
  private Integer vYear;

  @Column(name = "V_JNAME")
  private Integer vJname;

  @Column(name = "V_AUTHOR")
  private Integer vAuthor;

  @Column(name = "V_DOI")
  private Integer vDoi;

  @Column(name = "V_TITLE")
  private Integer vTitle;

  @Column(name = "C_YEAR")
  private Integer cYear;

  @Column(name = "C_JNAME")
  private String cJname;

  @Column(name = "C_AUTHOR")
  private String cAuthor;

  @Column(name = "C_DOI")
  private String cDoi;

  @Column(name = "C_TITLE")
  private String cTitle;

  @Column(name = "V_AUTHOR_CODE")
  private String vAuthorCode;

  @Column(name = "STAUTS")
  private Integer status;

  @Column(name = "INTERFACE_RESULT")
  private String interfaceResult;

  public SieTaskKpiVdDetailSplitPub() {
    super();
  }

  public SieTaskKpiVdDetailSplitPub(Long detailId, String uuId, String pNames, Integer year, String jName,
      String author, String title, String doi, String fundInfo, Integer vFundingInfo, String cFundingInfo,
      Integer vYear, Integer vJname, Integer vAuthor, Integer vDoi, Integer vTitle, Integer cYear, String cJname,
      String cAuthor, String cDoi, String cTitle, String vAuthorCode, Integer status, String interfaceResult) {
    super();
    this.detailId = detailId;
    this.uuId = uuId;
    this.pNames = pNames;
    this.year = year;
    this.jName = jName;
    this.author = author;
    this.title = title;
    this.doi = doi;
    this.fundInfo = fundInfo;
    this.vFundingInfo = vFundingInfo;
    this.cFundingInfo = cFundingInfo;
    this.vYear = vYear;
    this.vJname = vJname;
    this.vAuthor = vAuthor;
    this.vDoi = vDoi;
    this.vTitle = vTitle;
    this.cYear = cYear;
    this.cJname = cJname;
    this.cAuthor = cAuthor;
    this.cDoi = cDoi;
    this.cTitle = cTitle;
    this.vAuthorCode = vAuthorCode;
    this.status = status;
    this.interfaceResult = interfaceResult;
  }



  public Long getDetailId() {
    return detailId;
  }

  public void setDetailId(Long detailId) {
    this.detailId = detailId;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getpNames() {
    return pNames;
  }

  public void setpNames(String pNames) {
    this.pNames = pNames;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getjName() {
    return jName;
  }

  public void setjName(String jName) {
    this.jName = jName;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public Integer getvFundingInfo() {
    return vFundingInfo;
  }

  public void setvFundingInfo(Integer vFundingInfo) {
    this.vFundingInfo = vFundingInfo;
  }

  public String getcFundingInfo() {
    return cFundingInfo;
  }

  public void setcFundingInfo(String cFundingInfo) {
    this.cFundingInfo = cFundingInfo;
  }

  public Integer getvYear() {
    return vYear;
  }

  public void setvYear(Integer vYear) {
    this.vYear = vYear;
  }

  public Integer getvJname() {
    return vJname;
  }

  public void setvJname(Integer vJname) {
    this.vJname = vJname;
  }

  public Integer getvAuthor() {
    return vAuthor;
  }

  public void setvAuthor(Integer vAuthor) {
    this.vAuthor = vAuthor;
  }

  public Integer getvDoi() {
    return vDoi;
  }

  public void setvDoi(Integer vDoi) {
    this.vDoi = vDoi;
  }

  public Integer getvTitle() {
    return vTitle;
  }

  public void setvTitle(Integer vTitle) {
    this.vTitle = vTitle;
  }

  public Integer getcYear() {
    return cYear;
  }

  public void setcYear(Integer cYear) {
    this.cYear = cYear;
  }

  public String getcJname() {
    return cJname;
  }

  public void setcJname(String cJname) {
    this.cJname = cJname;
  }

  public String getcAuthor() {
    return cAuthor;
  }

  public void setcAuthor(String cAuthor) {
    this.cAuthor = cAuthor;
  }

  public String getcDoi() {
    return cDoi;
  }

  public void setcDoi(String cDoi) {
    this.cDoi = cDoi;
  }

  public String getcTitle() {
    return cTitle;
  }

  public void setcTitle(String cTitle) {
    this.cTitle = cTitle;
  }



  public String getvAuthorCode() {
    return vAuthorCode;
  }

  public void setvAuthorCode(String vAuthorCode) {
    this.vAuthorCode = vAuthorCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInterfaceResult() {
    return interfaceResult;
  }

  public void setInterfaceResult(String interfaceResult) {
    this.interfaceResult = interfaceResult;
  }

  @Override
  public String toString() {
    return "TmpKpiVdDetailSplitPub [detailId=" + detailId + ", uuId=" + uuId + ", pNames=" + pNames + ", year=" + year
        + ", jName=" + jName + ", author=" + author + ", title=" + title + ", doi=" + doi + ", vYear=" + vYear
        + ", vJname=" + vJname + ", vAuthor=" + vAuthor + ", vDoi=" + vDoi + ", vTitle=" + vTitle + ", cYear=" + cYear
        + ", cJname=" + cJname + ", cAuthor=" + cAuthor + ", cDoi=" + cDoi + ", cTitle=" + cTitle + ", status=" + status
        + ", interfaceResult=" + interfaceResult + "]";
  }



}
