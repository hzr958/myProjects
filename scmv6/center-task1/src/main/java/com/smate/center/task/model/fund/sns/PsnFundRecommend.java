package com.smate.center.task.model.fund.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员推荐基金的结果表记录实体.
 * 
 * @author zll
 * 
 */
@Entity
@Table(name = "PSN_FUND_RECOMMEND")
public class PsnFundRecommend implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5664791671837463612L;
  private Long id;// 主键.
  private Long fundId;// 基金ID.
  private Long psnId;// 人员ID.
  private double recommendation;// 推荐度.
  private Date creatDate;// 创建时间.
  private Date updateDate;// 更新时间.
  private int fundLevel;// 基金星级.
  private Integer isSendMail = 0;// 是否发送过邮件
  private Long agencyId; // 机构id，关联const_fund_agency表主键
  private String categoryViewName;// 基金类别列表显示名称
  private String agencyViewName;// 列表显示机构名称(根据当前语言环境显示相应中英文字段)
  private String fundGuideUrl;// 基金申报网址,对应scmpdwh.const_fund_category.guide_url值.
  private int isPriorFund;// 是否优先推荐基金_MJG_根据研究领域推荐基金的推广邮件发送 1-是 0-否.
  private Date fundStartDate;// 基金开始申报日期
  private Date fundEndDate;// 基金结束申报日期
  private String disName;// 研究领域

  public PsnFundRecommend() {}

  public PsnFundRecommend(Long fundId, Long psnId, double recommendation, Date creatDate) {
    this.fundId = fundId;
    this.psnId = psnId;
    this.recommendation = recommendation;
    this.creatDate = creatDate;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FUND_RECOMMEND", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RECOMMENDATION")
  public double getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(double recommendation) {
    this.recommendation = recommendation;
  }

  @Column(name = "CREAT_DATE")
  public Date getCreatDate() {
    return creatDate;
  }

  public void setCreatDate(Date creatDate) {
    this.creatDate = creatDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }


  @Column(name = "FUND_LEVEL")
  public int getFundLevel() {
    return fundLevel;
  }

  public void setFundLevel(int fundLevel) {
    this.fundLevel = fundLevel;
  }

  @Column(name = "IS_SEND_MAIL")
  public Integer getIsSendMail() {
    return isSendMail;
  }

  public void setIsSendMail(Integer isSendMail) {
    this.isSendMail = isSendMail;
  }

  @Transient
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Transient
  public String getAgencyViewName() {
    return agencyViewName;
  }

  public void setAgencyViewName(String agencyViewName) {
    this.agencyViewName = agencyViewName;
  }

  @Transient
  public String getCategoryViewName() {
    return categoryViewName;
  }

  public void setCategoryViewName(String categoryViewName) {
    this.categoryViewName = categoryViewName;
  }

  @Transient
  public String getFundGuideUrl() {
    return fundGuideUrl;
  }

  public void setFundGuideUrl(String fundGuideUrl) {
    this.fundGuideUrl = fundGuideUrl;
  }

  @Transient
  public int getIsPriorFund() {
    return isPriorFund;
  }

  public void setIsPriorFund(int isPriorFund) {
    this.isPriorFund = isPriorFund;
  }

  @Transient
  public Date getFundStartDate() {
    return fundStartDate;
  }

  public void setFundStartDate(Date fundStartDate) {
    this.fundStartDate = fundStartDate;
  }

  @Transient
  public Date getFundEndDate() {
    return fundEndDate;
  }

  public void setFundEndDate(Date fundEndDate) {
    this.fundEndDate = fundEndDate;
  }

  @Transient
  public String getDisName() {
    return disName;
  }

  public void setDisName(String disName) {
    this.disName = disName;
  }


}
