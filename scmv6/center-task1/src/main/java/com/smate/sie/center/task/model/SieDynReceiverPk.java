package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 动态接收者
 */
@Embeddable
public class SieDynReceiverPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4100917543491439352L;

  public SieDynReceiverPk() {
    super();
  }

  public SieDynReceiverPk(Long dynId, Long insId) {
    super();
    this.dynId = dynId;
    InsId = insId;
  }

  @Column(name = "DYN_ID")
  private Long dynId;

  @Column(name = "INS_ID")
  private Long InsId;

  public Long getDynId() {
    return dynId;
  }

  public Long getInsId() {
    return InsId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public void setInsId(Long insId) {
    InsId = insId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((InsId == null) ? 0 : InsId.hashCode());
    result = prime * result + ((dynId == null) ? 0 : dynId.hashCode());
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
    SieDynReceiverPk other = (SieDynReceiverPk) obj;
    if (InsId == null) {
      if (other.InsId != null)
        return false;
    } else if (!InsId.equals(other.InsId))
      return false;
    if (dynId == null) {
      if (other.dynId != null)
        return false;
    } else if (!dynId.equals(other.dynId))
      return false;
    return true;
  }

}
