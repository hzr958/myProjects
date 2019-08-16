package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author hd
 * @descript 人员统计表
 */
@Entity
@Table(name = "ST_PSN")
public class SiePsnStatistics implements Serializable {


  private static final long serialVersionUID = -5966956150300179762L;
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PRJ_SUM") // 项目数
  private Integer prjSum = 0;//
  @Column(name = "PUB_SUM") // 成果数
  private Integer pubSum = 0;
  @Column(name = "PT_SUM") // 专利数
  private Integer ptSum = 0;
  @Column(name = "PD_SUM") // 产品数
  private Integer pdSum = 0;
  @Column(name = "HINDEX") // 人员数
  private Integer hindex = 0;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public SiePsnStatistics() {
    super();
  }



  public SiePsnStatistics(Long psnId, Integer prjSum, Integer pubSum, Integer ptSum, Integer pdSum, Date updateDate) {
    super();
    this.psnId = psnId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.pdSum = pdSum;
    this.updateDate = updateDate;
  }



  public SiePsnStatistics(Long psnId, Integer prjSum, Integer pubSum, Integer ptSum, Date updateDate) {
    super();
    this.psnId = psnId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.updateDate = updateDate;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum == null ? 0 : prjSum;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum == null ? 0 : pubSum;
  }

  public Integer getPtSum() {
    return ptSum;
  }

  public void setPtSum(Integer ptSum) {
    this.ptSum = ptSum == null ? 0 : ptSum;
  }

  public Integer getPdSum() {
    return pdSum;
  }


  public void setPdSum(Integer pdSum) {
    this.pdSum = pdSum;
  }


  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex == null ? 0 : hindex;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
