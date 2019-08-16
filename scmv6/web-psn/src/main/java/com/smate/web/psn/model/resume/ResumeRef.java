package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息文献表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_REF")
public class ResumeRef implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1139474638269630169L;

  private ResumeRefKey id;

  public ResumeRef(Long confId, Long refId) {
    super();
    this.id = new ResumeRefKey(confId, refId);
  }

  public ResumeRef() {
    super();
  }

  @EmbeddedId
  public ResumeRefKey getId() {
    return id;
  }

  public void setId(ResumeRefKey id) {
    this.id = id;
  }

}
