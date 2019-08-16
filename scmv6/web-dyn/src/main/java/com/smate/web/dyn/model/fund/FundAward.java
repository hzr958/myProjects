package com.smate.web.dyn.model.fund;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.enums.converter.LikeStatusAttributeConverter;

/**
 * 基金赞操作
 * 
 * @author WSN
 *
 *         2017年8月18日 下午5:49:05
 *
 */
@Entity
@Table(name = "V_FUND_AWARD")
public class FundAward implements Serializable {

  private static final long serialVersionUID = 6847464708374461247L;
  private Long recordId; // 主键
  private Long fundId; // 基金ID
  private Long awardPsnId; // 赞人员ID
  private Date awardDate; // 赞操作时间
  @Convert(converter = LikeStatusAttributeConverter.class)
  private LikeStatusEnum status; // 赞状态：默认 0， 0：未赞， 1：已赞

  public FundAward() {
    super();
  }

  public FundAward(Long recordId, Long fundId, Long awardPsnId, Date awardDate, LikeStatusEnum status) {
    super();
    this.recordId = recordId;
    this.fundId = fundId;
    this.awardPsnId = awardPsnId;
    this.awardDate = awardDate;
    this.status = status;
  }

  public FundAward(Long fundId, Long awardPsnId, LikeStatusEnum status) {
    super();
    this.fundId = fundId;
    this.awardPsnId = awardPsnId;
    this.status = status;
  }

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(sequenceName = "SEQ_FUND_AWARD", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  @Column(name = "AWARD_PSN_ID")
  public Long getAwardPsnId() {
    return awardPsnId;
  }

  public void setAwardPsnId(Long awardPsnId) {
    this.awardPsnId = awardPsnId;
  }

  @Column(name = "AWARD_DATE")
  public Date getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(Date awardDate) {
    this.awardDate = awardDate;
  }

  @Column(name = "status")
  public LikeStatusEnum getStatus() {
    return status;
  }

  public void setStatus(LikeStatusEnum status) {
    this.status = status;
  }

}
