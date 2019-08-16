package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息教育经历表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumeEduKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4519011550869170774L;

  private Long confId;
  private Long eduId;

  public ResumeEduKey() {
    super();
  }

  public ResumeEduKey(Long confId, Long eduId) {
    super();
    this.confId = confId;
    this.eduId = eduId;
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "EDU_ID")
  public Long getEduId() {
    return eduId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((eduId == null) ? 0 : eduId.hashCode());
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
    ResumeEduKey other = (ResumeEduKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (eduId == null) {
      if (other.eduId != null)
        return false;
    } else if (!eduId.equals(other.eduId))
      return false;
    return true;
  }

}
