package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 公开信息文件表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ResumeFileKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 139646296124536485L;

  private Long confId;
  private Long fileId;

  public ResumeFileKey() {
    super();
  }

  public ResumeFileKey(Long confId, Long fileId) {
    super();
    this.confId = confId;
    this.fileId = fileId;
  }

  @Column(name = "CONF_ID")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
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
    ResumeFileKey other = (ResumeFileKey) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (fileId == null) {
      if (other.fileId != null)
        return false;
    } else if (!fileId.equals(other.fileId))
      return false;
    return true;
  }

}
