package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author fanzhiqiang
 * 
 */
@Embeddable
public class InsAliasId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6197109468328206919L;

  private Long insId;

  private Long dbId;

  public InsAliasId() {

  }

  public InsAliasId(Long insId, Long dbId) {
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
    if (!(other instanceof InsRefDbId))
      return false;
    InsRefDbId castOther = (InsRefDbId) other;

    return (this.getInsId() == castOther.getInsId() && this.getDbId() == castOther.getDbId());

  }

}
