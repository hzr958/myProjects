package com.smate.web.fund.agency.model;

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
 * 资助机构赞操作记录
 * 
 * @author wsn
 * @date Nov 13, 2018
 */

@Entity
@Table(name = "v_fund_agency_award")
public class FundAgencyAward implements Serializable {

  private static final long serialVersionUID = 6198951711698824345L;

  private Long id; // 主键
  private Long psnId; // 关注的人员ID
  private Long agencyId; // 赞、取消赞的资助机构ID
  private Date createDate; // 记录创建时间
  private Date updateDate; // 记录更新时间
  private Integer status; // 状态，1：赞， 0：取消赞

  public FundAgencyAward() {
    super();
  }

  public FundAgencyAward(Long id, Long psnId, Long agencyId, Date createDate, Date updateDate, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.agencyId = agencyId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_AGENCY_AWARD", allocationSize = 1)
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

  @Column(name = "AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
