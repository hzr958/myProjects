package com.smate.center.task.model.bdsp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PATENT_BASE")
public class BdspPatentBase implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 申请年份
   */
  @Column(name = "PRP_YEAR")
  private Integer prpYear;
  /**
   * 专利申请号
   */
  @Column(name = "APPLY_ID")
  private Long applyId;
  /**
   * 类型
   */
  @Column(name = "PUB_TYPE")
  private Integer pubType;
  /**
   * 项目资助号
   */
  @Column(name = "SUPPORT_ID")
  private String supportId;
  @Column(name = "FUNDINFO")
  private String fundinfo;
  @Column(name = "PATENT_NO")
  private String patentNo;
  @Column(name = "CREATE_DATE")
  private Date createDate;


  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getFundinfo() {
    return fundinfo;
  }

  public void setFundinfo(String fundinfo) {
    this.fundinfo = fundinfo;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getPrpYear() {
    return prpYear;
  }

  public void setPrpYear(Integer prpYear) {
    this.prpYear = prpYear;
  }

  public Long getApplyId() {
    return applyId;
  }

  public void setApplyId(Long applyId) {
    this.applyId = applyId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getSupportId() {
    return supportId;
  }

  public void setSupportId(String supportId) {
    this.supportId = supportId;
  }

  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }



}
