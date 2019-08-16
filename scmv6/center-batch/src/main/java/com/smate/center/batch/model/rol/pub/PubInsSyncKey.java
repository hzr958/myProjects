package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * 
 */
@Embeddable
public class PubInsSyncKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5356575973295382999L;
  // 成果ID
  private Long snsPubId;
  // 成果单位ID
  private Long insId;

  public PubInsSyncKey() {
    super();
  }

  public PubInsSyncKey(Long snsPubId, Long insId) {
    super();
    this.snsPubId = snsPubId;
    this.insId = insId;
  }

  @Column(name = "SNS_PUB_ID")
  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((snsPubId == null) ? 0 : snsPubId.hashCode());
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
    PubInsSyncKey other = (PubInsSyncKey) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (snsPubId == null) {
      if (other.snsPubId != null)
        return false;
    } else if (!snsPubId.equals(other.snsPubId))
      return false;
    return true;
  }
}
