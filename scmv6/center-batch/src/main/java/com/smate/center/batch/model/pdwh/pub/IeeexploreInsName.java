package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位IEEEXplore别名表.
 * 
 * @author linyueqin
 * 
 */
@Entity
@Table(name = "IEEEXPLORE_INS_NAME")
public class IeeexploreInsName implements Serializable {

  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String ieeexploreName;
  private Integer ieeexplorenLength;
  private Integer freq = 0;
  private Date lastUse;

  public IeeexploreInsName() {
    super();
  }

  public IeeexploreInsName(Long id, Long insId, String ieeexploreName, Integer ieeexplorenLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.ieeexploreName = ieeexploreName;
    this.ieeexplorenLength = ieeexplorenLength;
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

  @Column(name = "IEEEXPLORE_NAME")
  public String getIeeexploreName() {
    return ieeexploreName;
  }

  public void setIeeexploreName(String ieeexploreName) {
    this.ieeexploreName = ieeexploreName;
  }

  @Column(name = "IEEEXPLOREN_LENGTH")
  public Integer getIeeexplorenLength() {
    return ieeexplorenLength;
  }

  public void setIeeexplorenLength(Integer ieeexplorenLength) {
    this.ieeexplorenLength = ieeexplorenLength;
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
