package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息项目表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumePrjKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 139646296124536485L;

  private Long confId;
  private Long prjId;

  public ResumePrjKey() {
    super();
  }

  public ResumePrjKey(Long confId, Long prjId) {
    super();
    this.confId = confId;
    this.prjId = prjId;
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((prjId == null) ? 0 : prjId.hashCode());
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
    ResumePrjKey other = (ResumePrjKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (prjId == null) {
      if (other.prjId != null)
        return false;
    } else if (!prjId.equals(other.prjId))
      return false;
    return true;
  }

}
