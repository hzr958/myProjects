package com.smate.center.batch.model.pdwh.pub.cnipr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位CNIPR别名表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNIPR_INS_NAME")
public class CniprInsName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1761814838329371888L;
  private Long id;
  private Long insId;
  private String cniprName;
  private String lowerName;
  private Integer cniprnLength;
  private Integer freq = 0;
  private Date lastUse;

  public CniprInsName() {
    super();
  }

  public CniprInsName(Long id, Long insId, String lowerName, Integer cniprnLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.lowerName = lowerName;
    this.cniprnLength = cniprnLength;
  }

  public CniprInsName(Long id, Long insId, String cnkiName, String lowerName, Integer cniprnLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.cniprName = cnkiName;
    this.lowerName = lowerName;
    this.cniprnLength = cniprnLength;
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

  @Column(name = "LOWER_NAME")
  public String getLowerName() {
    return lowerName;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  @Column(name = "CNIPR_NAME")
  public String getCniprName() {
    return cniprName;
  }

  @Column(name = "CNIPRN_LENGTH")
  public Integer getCniprnLength() {
    return cniprnLength;
  }

  public void setCniprName(String cniprName) {
    this.cniprName = cniprName;
  }

  public void setCniprnLength(Integer cniprnLength) {
    this.cniprnLength = cniprnLength;
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

  public void setLowerName(String lowerName) {
    this.lowerName = lowerName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((cniprnLength == null) ? 0 : cniprnLength.hashCode());
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
    CniprInsName other = (CniprInsName) obj;
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
    if (cniprnLength == null) {
      if (other.cniprnLength != null)
        return false;
    } else if (!cniprnLength.equals(other.cniprnLength))
      return false;
    if (lowerName == null) {
      if (other.lowerName != null)
        return false;
    } else if (!lowerName.equals(other.lowerName))
      return false;
    return true;
  }

}
