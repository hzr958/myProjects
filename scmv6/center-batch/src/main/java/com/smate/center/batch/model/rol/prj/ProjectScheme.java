package com.smate.center.batch.model.rol.prj;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 资助类别. SIE数据库
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "PROJECT_SCHEME")
public class ProjectScheme implements Serializable {
  /**
   * @author liangguokeng
   */
  private static final long serialVersionUID = -4228111234998516363L;
  // 项目id
  private Long id;
  // 所有者(对应 person的psn_id)
  private Long psnId;
  // 资金总数
  private BigDecimal amount;
  // 资助机构名称
  private String agencyName;
  // 资助机构名称
  private String agencyEnName;
  // 资助类别名称
  private String schemeName;
  // 资助类别名称
  private String schemeEnName;
  // 开始年份
  private Integer startYear;
  // 开始月份
  private Integer startMonth;
  // 时间查询startYear*100+startMonth
  private Integer searchTime;
  // 项目年度
  private Integer fundingYear;

  private Integer numOfProject;

  private String amountView;

  private Integer noAgency;

  public ProjectScheme() {
    super();
  }

  @Id
  @Column(name = "PROJECT_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "AMOUNT")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Column(name = "SCHEME_AGENCY_NAME")
  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  @Column(name = "SCHEME_NAME")
  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  @Column(name = "START_YEAR")
  public Integer getStartYear() {
    return startYear;
  }

  @Column(name = "START_MONTH")
  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  @Column(name = "SEARCH_TIME")
  public Integer getSearchTime() {
    return searchTime;
  }

  public void setSearchTime(Integer searchTime) {
    this.searchTime = searchTime;
  }

  @Column(name = "FUNDING_YEAR")
  public Integer getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(Integer fundingYear) {
    this.fundingYear = fundingYear;
  }

  @Transient
  public Integer getNumOfProject() {
    return numOfProject;
  }

  public void setNumOfProject(Integer numOfProject) {
    this.numOfProject = numOfProject;
  }

  @Column(name = "SCHEME_AGENCY_NAME_EN")
  public String getAgencyEnName() {
    return agencyEnName;
  }

  @Column(name = "SCHEME_NAME_EN")
  public String getSchemeEnName() {
    return schemeEnName;
  }

  public void setAgencyEnName(String agencyEnName) {
    this.agencyEnName = agencyEnName;
  }

  public void setSchemeEnName(String schemeEnName) {
    this.schemeEnName = schemeEnName;
  }

  @Transient
  public String getAmountView() {
    return amountView;
  }

  public void setAmountView(String amountView) {
    this.amountView = amountView;
  }

  @Transient
  public Integer getNoAgency() {
    return noAgency;
  }

  public void setNoAgency(Integer noAgency) {
    this.noAgency = noAgency;
  }

}
