package com.smate.center.open.model.group.prj;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组项目扩展表
 * 
 * @author lhd
 */
@Entity
@Table(name = "GROUP_PRJ_EXTEND")
public class GroupPrjExtend implements Serializable {

  private static final long serialVersionUID = -4740684833479279130L;
  @Id
  @Column(name = "GROUP_ID")
  private Long groupId;// 群组编号
  @Column(name = "FUNDING_INS")
  private String fundingIns;// 资助机构
  @Column(name = "FUNDING_TYPES")
  private String fundingTypes;// 资助类别
  @Column(name = "AMOUNT")
  private BigDecimal amount;// 资助金额
  @Column(name = "CURRENCY")
  private String currency;// 币种
  @Column(name = "START_YEAR")
  private Integer startYear;// 开始年份
  @Column(name = "START_MONTH")
  private Integer startMonth;// 开始月份
  @Column(name = "START_DAY")
  private Integer startDay;// 开始日期
  @Column(name = "END_YEAR")
  private Integer endYear;// 结束年份
  @Column(name = "END_MONTH")
  private Integer endMonth;// 结束月份
  @Column(name = "END_DAY")
  private Integer endDay;// 结束日期

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getFundingIns() {
    return fundingIns;
  }

  public void setFundingIns(String fundingIns) {
    this.fundingIns = fundingIns;
  }

  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  public Integer getStartDay() {
    return startDay;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public GroupPrjExtend() {}


}
