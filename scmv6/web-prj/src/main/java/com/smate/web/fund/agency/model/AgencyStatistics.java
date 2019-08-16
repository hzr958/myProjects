package com.smate.web.fund.agency.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 资助机构操作统计表
 * 
 * @author wsn
 * @date Nov 13, 2018
 */

@Entity
@Table(name = "V_AGENCY_STATISTICS")
public class AgencyStatistics implements Serializable {

  private static final long serialVersionUID = 6992406184914272432L;

  private Long agencyId; // 资助机构ID
  private Long awardSum; // 赞操作总数
  private Long shareSum; // 分享操作总数
  private Long interestSum; // 关注总数

  public AgencyStatistics() {
    super();
  }

  public AgencyStatistics(Long agencyId, Long awardSum, Long shareSum, Long interestSum) {
    super();
    this.agencyId = agencyId;
    this.awardSum = awardSum;
    this.shareSum = shareSum;
    this.interestSum = interestSum;
  }

  @Id
  @Column(name = "AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Column(name = "AWARD_SUM")
  public Long getAwardSum() {
    return awardSum;
  }

  public void setAwardSum(Long awardSum) {
    this.awardSum = awardSum;
  }

  @Column(name = "SHARE_SUM")
  public Long getShareSum() {
    return shareSum;
  }

  public void setShareSum(Long shareSum) {
    this.shareSum = shareSum;
  }

  @Column(name = "INTEREST_SUM")
  public Long getInterestSum() {
    return interestSum;
  }

  public void setInterestSum(Long interestSum) {
    this.interestSum = interestSum;
  }

}
