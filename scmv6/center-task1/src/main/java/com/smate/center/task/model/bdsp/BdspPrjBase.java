package com.smate.center.task.model.bdsp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PRJ_BASE")
public class BdspPrjBase implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * 项目id
   */
  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;
  /**
   * 申请id
   */
  @Column(name = "APPLY_ID")
  private Long applyId;
  /**
   * 承担单位
   */
  @Column(name = "ORG_ID")
  private Long orgId;
  /**
   * 项目负责人
   */
  @Column(name = "PSN_ID")
  private Long psnId;
  /**
   * 项目资助号
   */
  @Column(name = "SUPPORT_ID")
  private String supportId;
  /**
   * 年份
   */
  @Column(name = "STAT_YEAR")
  private String statYear;
  /**
   * 金额
   */
  @Column(name = "TOTAL_AMT")
  private Double totalAmt;
  /**
   * 类别
   */
  @Column(name = "GRANT_ID")
  private Long grantId;
  @Column(name = "CREATE_DATE")
  private Date createDate;


  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getApplyId() {
    return applyId;
  }

  public void setApplyId(Long applyId) {
    this.applyId = applyId;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getSupportId() {
    return supportId;
  }

  public void setSupportId(String supportId) {
    this.supportId = supportId;
  }

  public String getStatYear() {
    return statYear;
  }

  public void setStatYear(String statYear) {
    this.statYear = statYear;
  }

  public Double getTotalAmt() {
    return totalAmt;
  }

  public void setTotalAmt(Double totalAmt) {
    this.totalAmt = totalAmt;
  }

  public Long getGrantId() {
    return grantId;
  }

  public void setGrantId(Long grantId) {
    this.grantId = grantId;
  }



}
