package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * PubInsId.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class PubInsId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5356575973295382999L;
  // 成果ID
  private Long pubId;
  // 成果单位ID
  private Long insId;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

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
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
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
    PubInsId other = (PubInsId) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    return true;
  }

}
