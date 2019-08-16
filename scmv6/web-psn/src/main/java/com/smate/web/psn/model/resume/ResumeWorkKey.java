package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息工作经历表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumeWorkKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6899365479307931561L;

  private Long confId;
  private Long workId;

  public ResumeWorkKey() {
    super();
  }

  public ResumeWorkKey(Long confId, Long workId) {
    super();
    this.confId = confId;
    this.workId = workId;
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "WORK_ID")
  public Long getWorkId() {
    return workId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((workId == null) ? 0 : workId.hashCode());
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
    ResumeWorkKey other = (ResumeWorkKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }

}
