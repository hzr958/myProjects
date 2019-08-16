package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果项目信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_FUNDINFO")
public class PubFundInfoRol implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4877825881899252276L;

  // 成果ID
  private Long pubId;
  // 单位ID
  private Long insId;
  // 基金信息原始
  private String fundInfo;
  // 基金信息小写
  private String lfundInfo;
  // 基金信息hashcode用于判断是否修改
  private Long fundHash;

  public PubFundInfoRol() {
    super();
  }

  public PubFundInfoRol(Long pubId, String fundInfo) {
    super();
    this.pubId = pubId;
    this.fundInfo = fundInfo;
  }

  public PubFundInfoRol(Long pubId, Long insId, String fundInfo, String lfundInfo, Long fundHash) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.fundInfo = fundInfo;
    this.lfundInfo = lfundInfo;
    this.fundHash = fundHash;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "FUNDINFO")
  public String getFundInfo() {
    return fundInfo;
  }

  @Column(name = "LFUNDINFO")
  public String getLfundInfo() {
    return lfundInfo;
  }

  @Column(name = "FUND_HASH")
  public Long getFundHash() {
    return fundHash;
  }

  public void setFundHash(Long fundHash) {
    this.fundHash = fundHash;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public void setLfundInfo(String lfundInfo) {
    this.lfundInfo = lfundInfo;
  }

}
