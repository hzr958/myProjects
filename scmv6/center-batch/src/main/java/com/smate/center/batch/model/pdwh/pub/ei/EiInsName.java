package com.smate.center.batch.model.pdwh.pub.ei;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位ei别名表.
 * 
 * @author linyueqin
 * 
 */
@Entity
@Table(name = "EI_INS_NAME")
public class EiInsName implements Serializable {

  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String eiName;
  private Integer einLength;
  private Integer freq = 0;
  private Date lastUse;

  public EiInsName() {
    super();
  }

  public EiInsName(Long id, Long insId, String eiName, Integer einLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.eiName = eiName;
    this.einLength = einLength;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "EI_NAME")
  public String getEiName() {
    return eiName;
  }

  public void setEiName(String eiName) {
    this.eiName = eiName;
  }

  @Column(name = "EIN_LENGTH")
  public Integer getEinLength() {
    return einLength;
  }

  public void setEinLength(Integer einLength) {
    this.einLength = einLength;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

}
