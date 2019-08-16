package com.smate.core.base.utils.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author lichangwen
 * 
 */
@Embeddable
public class PsnInsPk implements Serializable {
  private static final long serialVersionUID = -9145054634750598435L;
  /** 人员Id. */
  private Long psnId;
  /** 单位Id. */
  private Long insId;

  public PsnInsPk() {

  }

  public PsnInsPk(Long psnId, Long insId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
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
    PsnInsPk other = (PsnInsPk) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
