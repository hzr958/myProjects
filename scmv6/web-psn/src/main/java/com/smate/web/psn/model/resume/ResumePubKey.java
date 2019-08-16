package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息成果表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumePubKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5188127817145630147L;

  // 简历设置主键
  private Long confId;
  // 成果ID
  private Long pubId;

  public ResumePubKey() {
    super();
  }

  public ResumePubKey(Long confId, Long pubId) {
    super();
    this.confId = confId;
    this.pubId = pubId;
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
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
    ResumePubKey other = (ResumePubKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    return true;
  }

}
