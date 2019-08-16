package com.smate.center.batch.model.tmp.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * isi成果导出表，关键词，abstract
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PUB_FUNDING_INFO")
public class PubFundingInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1143838657462494973L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "DB_ID")
  private Integer dbId;

  @Column(name = "FUNDING_INFO")
  private String fundingInfo;

  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getFundingInfo() {
    return fundingInfo;
  }

  public void setFundingInfo(String fundingInfo) {
    this.fundingInfo = fundingInfo;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

}
