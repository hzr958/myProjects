package com.smate.web.dyn.model.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金操作统计
 * 
 * @author WSN
 *
 *         2017年8月18日 下午4:50:33
 *
 */
@Entity
@Table(name = "V_FUND_STATISTICS")
public class FundStatistics implements Serializable {

  private static final long serialVersionUID = -478774149602197637L;

  private Long fundId; // 基金ID
  private Integer awardCount; // 赞统计数
  private Integer shareCount; // 分享统计数

  public FundStatistics() {
    super();
  }

  public FundStatistics(Long fundId, Integer awardCount, Integer shareCount) {
    super();
    this.fundId = fundId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
  }

  @Id
  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  @Column(name = "AWARD_COUNT")
  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  @Column(name = "SHARE_COUNT")
  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

}
