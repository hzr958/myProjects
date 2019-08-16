package com.smate.web.v8pub.vo.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author fanzhiqiang
 * 
 */
@Embeddable
public class InsRefDbId implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7912822229429995237L;
  // Institution ID (refer to [Institution] table).
  private Long insId;
  // RefDB ID (refer to [RefDB] table).
  private Long dbId;

  public InsRefDbId() {}

  public InsRefDbId(Long insId, Long dbId) {
    this.insId = insId;
    this.dbId = dbId;
  }

  // Property accessors

  @Column(name = "INS_ID")
  public Long getInsId() {
    return this.insId;
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
    if (!(other instanceof InsRefDbId))
      return false;
    InsRefDbId castOther = (InsRefDbId) other;

    return (this.getInsId().equals(castOther.getInsId()) && this.getDbId().equals(castOther.getDbId()));

  }

}
