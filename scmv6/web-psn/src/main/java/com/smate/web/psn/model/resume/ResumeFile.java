package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息文件表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_FILE")
public class ResumeFile implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 19493347591262844L;

  private ResumeFileKey id;

  public ResumeFile() {
    super();
  }

  public ResumeFile(Long confId, Long fileId) {
    super();
    this.id = new ResumeFileKey(confId, fileId);
  }

  @EmbeddedId
  public ResumeFileKey getId() {
    return id;
  }

  public void setId(ResumeFileKey id) {
    this.id = id;
  }

}
