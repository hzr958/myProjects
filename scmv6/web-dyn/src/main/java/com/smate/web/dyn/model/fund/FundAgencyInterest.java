package com.smate.web.dyn.model.fund;

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
 * 关注的资助机构
 * 
 * @author wsn
 * @date Nov 13, 2018
 */

@Entity
@Table(name = "V_FUND_AGENCY_INTEREST")
public class FundAgencyInterest implements Serializable {

  private static final long serialVersionUID = -4444951012500410293L;

  private Long id; // 主键
  private Long psnId; // 关注的人员ID
  private Long agencyId; // 关注的资助机构ID
  private Date createDate; // 记录创建时间
  private Date updateDate; // 记录更新时间
  private Integer status; // 状态，1：关注， 0：取消关注
  private String showName;// 机构名称

  public FundAgencyInterest() {
    super();
  }

  public FundAgencyInterest(Long id, Long psnId, Long agencyId, Date createDate, Date updateDate, Integer status) {
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
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_AGENCY_INTEREST", allocationSize = 1)
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

  @Transient
  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

}
