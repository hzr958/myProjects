package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位ScienceDirect别名表.
 * 
 * @author linyueqin
 * 
 */
@Entity
@Table(name = "SCIENCEDIRECT_INS_NAME")
public class SciencedirectInsName implements Serializable {

  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String sciencedirectName;
  private Integer sciencedirectnLength;
  private Integer freq = 0;
  private Date lastUse;

  public SciencedirectInsName() {
    super();
  }

  public SciencedirectInsName(Long id, Long insId, String sciencedirectName, Integer sciencedirectnLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.sciencedirectName = sciencedirectName;
    this.sciencedirectnLength = sciencedirectnLength;
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

  @Column(name = "SCIENCEDIRECT_NAME")
  public String getSciencedirectName() {
    return sciencedirectName;
  }

  public void setSciencedirectName(String sciencedirectName) {
    this.sciencedirectName = sciencedirectName;
  }

  @Column(name = "SCIENCEDIRECTN_LENGTH")
  public Integer getSciencedirectnLength() {
    return sciencedirectnLength;
  }

  public void setSciencedirectnLength(Integer sciencedirectnLength) {
    this.sciencedirectnLength = sciencedirectnLength;
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
