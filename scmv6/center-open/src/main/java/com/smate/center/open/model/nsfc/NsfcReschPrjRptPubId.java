package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NsfcReschPrjRptPubId implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1583630019904976910L;
  private Long rptId;
  private Long pubId;

  // Constructors

  /** default constructor */
  public NsfcReschPrjRptPubId() {}

  /** full constructor */
  public NsfcReschPrjRptPubId(Long rptId, Long pubId) {
    this.rptId = rptId;
    this.pubId = pubId;
  }

  // Property accessors

  @Column(name = "RPT_ID", nullable = false, precision = 22, scale = 0)
  public Long getRptId() {
    return this.rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  @Column(name = "PUB_ID", nullable = false, precision = 22, scale = 0)
  public Long getPubId() {
    return this.pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof NsfcReschPrjRptPubId))
      return false;
    NsfcReschPrjRptPubId castOther = (NsfcReschPrjRptPubId) other;

    return ((this.getRptId() == castOther.getRptId())
        || (this.getRptId() != null && castOther.getRptId() != null && this.getRptId().equals(castOther.getRptId())))
        && ((this.getPubId() == castOther.getPubId()) || (this.getPubId() != null && castOther.getPubId() != null
            && this.getPubId().equals(castOther.getPubId())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (getRptId() == null ? 0 : this.getRptId().hashCode());
    result = 37 * result + (getPubId() == null ? 0 : this.getPubId().hashCode());
    return result;
  }
}
