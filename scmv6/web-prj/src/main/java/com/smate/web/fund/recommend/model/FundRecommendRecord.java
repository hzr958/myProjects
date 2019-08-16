package com.smate.web.fund.recommend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金推荐操作记录
 * 
 * @author yhx
 *
 */
@Entity
@Table(name = "V_FUND_RECOMMEND_RECORD")
public class FundRecommendRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "V_SEQ_FUND_RECOMMEND_RECORD", sequenceName = "V_SEQ_FUND_RECOMMEND_RECORD",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_FUND_RECOMMEND_RECORD")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PSN_ID")
  private Long psnId; // 人员id

  @Column(name = "FUND_ID")
  private Long fundId; // 基金id

  @Column(name = "STATUS")
  private Integer status; // 状态：0正常，1不感兴趣

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 被更新时间

  public FundRecommendRecord() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

}
