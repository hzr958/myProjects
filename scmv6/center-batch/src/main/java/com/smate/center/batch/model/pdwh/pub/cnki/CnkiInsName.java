package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位CNKI别名表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_INS_NAME")
public class CnkiInsName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 547455179284889584L;
  private Long id;
  private Long insId;
  private String cnkiName;
  private String lowerName;
  private Integer cnkinLength;
  private Integer freq = 0;
  private Date lastUse;

  public CnkiInsName() {
    super();
  }

  public CnkiInsName(Long id, Long insId, String lowerName, Integer cnkinLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.lowerName = lowerName;
    this.cnkinLength = cnkinLength;
  }

  public CnkiInsName(Long id, Long insId, String cnkiName, String lowerName, Integer cnkinLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.cnkiName = cnkiName;
    this.lowerName = lowerName;
    this.cnkinLength = cnkinLength;
    this.freq = 0;
    this.lastUse = new Date();
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

  @Column(name = "CNKI_NAME")
  public String getCnkiName() {
    return cnkiName;
  }

  @Column(name = "LOWER_NAME")
  public String getLowerName() {
    return lowerName;
  }

  @Column(name = "CNKIN_LENGTH")
  public Integer getCnkinLength() {
    return cnkinLength;
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

  public void setCnkiName(String cnkiName) {
    this.cnkiName = cnkiName;
  }

  public void setLowerName(String lowerName) {
    this.lowerName = lowerName;
  }

  public void setCnkinLength(Integer cnkinLength) {
    this.cnkinLength = cnkinLength;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((cnkinLength == null) ? 0 : cnkinLength.hashCode());
    result = prime * result + ((lowerName == null) ? 0 : lowerName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CnkiInsName other = (CnkiInsName) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (cnkinLength == null) {
      if (other.cnkinLength != null)
        return false;
    } else if (!cnkinLength.equals(other.cnkinLength))
      return false;
    if (lowerName == null) {
      if (other.lowerName != null)
        return false;
    } else if (!lowerName.equals(other.lowerName))
      return false;
    return true;
  }

}
