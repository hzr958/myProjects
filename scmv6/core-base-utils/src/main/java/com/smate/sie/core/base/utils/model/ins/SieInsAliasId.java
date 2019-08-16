package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 检索式表主键
 * 
 * @author fanzhiqiang
 * 
 */
@Embeddable
public class SieInsAliasId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6077149138063403540L;

  private Long insId;

  private Long dbId;

  public SieInsAliasId() {

  }

  public SieInsAliasId(Long insId, Long dbId) {
    this.insId = insId;
    this.dbId = dbId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((dbId == null) ? 0 : dbId.hashCode());
    return result;
  }

  /**
  * 
  */
  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof SieInsRefDbId))
      return false;
    SieInsRefDbId castOther = (SieInsRefDbId) other;

    return (this.getInsId() == castOther.getInsId() && this.getDbId() == castOther.getDbId());

  }
}
