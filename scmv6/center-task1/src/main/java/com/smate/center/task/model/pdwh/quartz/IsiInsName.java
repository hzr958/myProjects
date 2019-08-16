package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位ISI别名表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_INS_NAME")
public class IsiInsName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 547455179284889584L;
  private Long id;
  private Long insId;
  private String isiName;
  private Integer isinLength;
  private Integer freq = 0;
  private Date lastUse;

  public IsiInsName() {
    super();
  }

  public IsiInsName(Long id, Long insId, String isiName, Integer isinLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.isiName = isiName;
    this.isinLength = isinLength;
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

  @Column(name = "ISI_NAME")
  public String getIsiName() {
    return isiName;
  }

  @Column(name = "ISIN_LENGTH")
  public Integer getIsinLength() {
    return isinLength;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
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

  public void setIsiName(String isiName) {
    this.isiName = isiName;
  }

  public void setIsinLength(Integer isinLength) {
    this.isinLength = isinLength;
  }

}
