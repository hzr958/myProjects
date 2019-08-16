package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息教育经历表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_EDU")
public class ResumeEdu implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3114489125078988393L;

  private ResumeEduKey id;

  public ResumeEdu() {
    super();
  }

  public ResumeEdu(Long confId, Long eduId) {
    super();
    this.id = new ResumeEduKey(confId, eduId);
  }

  @EmbeddedId
  public ResumeEduKey getId() {
    return id;
  }

  public void setId(ResumeEduKey id) {
    this.id = id;
  }

}
