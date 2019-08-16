package com.smate.center.batch.model.pdwh.pub.sps;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位scopus别名表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_INS_NAME")
public class SpsInsName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String spsName;
  private Integer spsnLength;
  private Integer freq = 0;
  private Date lastUse;

  public SpsInsName() {
    super();
  }

  public SpsInsName(Long id, Long insId, String spsName, Integer spsnLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.spsName = spsName;
    this.spsnLength = spsnLength;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  @Column(name = "SPS_NAME")
  public String getSpsName() {
    return spsName;
  }

  @Column(name = "SPSN_LENGTH")
  public Integer getSpsnLength() {
    return spsnLength;
  }

  public void setSpsName(String spsName) {
    this.spsName = spsName;
  }

  public void setSpsnLength(Integer spsnLength) {
    this.spsnLength = spsnLength;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
