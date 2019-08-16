package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息工作经历表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_WORK")
public class ResumeWork implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5877454729724295686L;

  private ResumeWorkKey id;

  public ResumeWork() {
    super();
  }

  public ResumeWork(Long confId, Long workId) {
    super();
    this.id = new ResumeWorkKey(confId, workId);
  }

  @EmbeddedId
  public ResumeWorkKey getId() {
    return id;
  }

  public void setId(ResumeWorkKey id) {
    this.id = id;
  }

}
