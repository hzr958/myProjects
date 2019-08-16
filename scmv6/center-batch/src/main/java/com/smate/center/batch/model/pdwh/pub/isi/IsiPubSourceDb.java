package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * isi成果数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_PUB_SOURCE_DB")
public class IsiPubSourceDb implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5505602455275375430L;

  private Long pubId;
  // 是否是scie库成果
  private Integer sci = 0;
  // 是否是ssci库成果
  private Integer ssci = 0;
  // 是否是istp库成果
  private Integer istp = 0;

  public IsiPubSourceDb() {
    super();
  }

  public IsiPubSourceDb(Long pubId) {
    super();
    this.pubId = pubId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "SCI")
  public Integer getSci() {
    return sci;
  }

  @Column(name = "SSCI")
  public Integer getSsci() {
    return ssci;
  }

  @Column(name = "ISTP")
  public Integer getIstp() {
    return istp;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setSci(Integer sci) {
    this.sci = sci;
  }

  public void setSsci(Integer ssci) {
    this.ssci = ssci;
  }

  public void setIstp(Integer istp) {
    this.istp = istp;
  }

}
