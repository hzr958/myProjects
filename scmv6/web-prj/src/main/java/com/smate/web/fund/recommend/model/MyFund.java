package com.smate.web.fund.recommend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 我的（收藏的）基金
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:44:16
 *
 */

@Entity
@Table(name = "V_MY_FUND")
public class MyFund {

  private Long id; // 主键
  private Long psnId; // 人员ID
  private Long fundId; // 基金ID
  private Date collectTime; // 收藏的时间

  public MyFund() {
    super();
  }

  public MyFund(Long id, Long psnId, Long fundId, Date collectTime) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.fundId = fundId;
    this.collectTime = collectTime;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MY_FUND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  @Column(name = "COLLECT_TIME")
  public Date getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(Date collectTime) {
    this.collectTime = collectTime;
  }

}
