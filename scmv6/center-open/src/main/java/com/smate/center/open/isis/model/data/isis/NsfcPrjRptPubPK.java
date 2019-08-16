package com.smate.center.open.isis.model.data.isis;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the NSFC_PRJ_RPT_PUB database table.
 * 
 * @author hp
 * @date 2015-10-27
 */
@Embeddable
public class NsfcPrjRptPubPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7688719334237884931L;

  @Column(name = "RPT_ID")
  private long rptId;

  @Column(name = "PUB_ID")
  private long pubId;

  public NsfcPrjRptPubPK() {}

  public long getRptId() {
    return this.rptId;
  }

  public void setRptId(long rptId) {
    this.rptId = rptId;
  }

  public long getPubId() {
    return this.pubId;
  }

  public void setPubId(long pubId) {
    this.pubId = pubId;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof NsfcPrjRptPubPK)) {
      return false;
    }
    NsfcPrjRptPubPK castOther = (NsfcPrjRptPubPK) other;
    return (this.rptId == castOther.rptId) && (this.pubId == castOther.pubId);
  }

  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + ((int) (this.rptId ^ (this.rptId >>> 32)));
    hash = hash * prime + ((int) (this.pubId ^ (this.pubId >>> 32)));
    return hash;
  }
}
