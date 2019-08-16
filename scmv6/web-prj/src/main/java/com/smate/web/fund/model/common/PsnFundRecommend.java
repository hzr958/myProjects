package com.smate.web.fund.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


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


  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "RECOMMENDATION")
  public double getRecommendation() {
    return recommendation;
  }

  @Column(name = "CREAT_DATE")
  public Date getCreatDate() {
    return creatDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "FUND_LEVEL")
  public int getFundLevel() {
    return fundLevel;
  }

  @Column(name = "IS_SEND_MAIL")
  public Integer getIsSendMail() {
    return isSendMail;
  }

  public void setIsSendMail(Integer isSendMail) {
    this.isSendMail = isSendMail;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setRecommendation(double recommendation) {
    this.recommendation = recommendation;
  }

  public void setCreatDate(Date creatDate) {
    this.creatDate = creatDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setFundLevel(int fundLevel) {
    this.fundLevel = fundLevel;
  }

}


