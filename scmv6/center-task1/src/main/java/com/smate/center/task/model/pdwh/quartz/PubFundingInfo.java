package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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

  public PubFundingInfo() {
    super();
  }

  public PubFundingInfo(Long pubId, Integer dbId, String fundingInfo) {
    super();
    this.pubId = pubId;
    this.dbId = dbId;
    this.fundingInfo = fundingInfo;
  }

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


}
