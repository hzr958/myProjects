package com.smate.web.psn.model.resume;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息文献表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumeRefKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6870440972084038896L;

  // 简历设置主键
  private Long confId;
  // 文献ID
  private Long refId;

  public ResumeRefKey(Long confId, Long refId) {
    super();
    this.confId = confId;
    this.refId = refId;
  }

  public ResumeRefKey() {
    super();
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "REF_ID")
  public Long getRefId() {
    return refId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setRefId(Long refId) {
    this.refId = refId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((refId == null) ? 0 : refId.hashCode());
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
    ResumeRefKey other = (ResumeRefKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (refId == null) {
      if (other.refId != null)
        return false;
    } else if (!refId.equals(other.refId))
      return false;
    return true;
  }

}
