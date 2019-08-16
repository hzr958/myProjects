package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息项目表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_PRJ")
public class ResumePrj implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2372669046230519722L;

  private ResumePrjKey id;

  public ResumePrj() {
    super();
  }

  public ResumePrj(Long confId, Long prjId) {
    super();
    this.id = new ResumePrjKey(confId, prjId);
  }

  @EmbeddedId
  public ResumePrjKey getId() {
    return id;
  }

  public void setId(ResumePrjKey id) {
    this.id = id;
  }

}
