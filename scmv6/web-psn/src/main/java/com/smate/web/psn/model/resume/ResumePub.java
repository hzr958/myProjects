package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公开信息成果表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_PUB")
public class ResumePub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -711058594017138262L;

  private ResumePubKey id;

  public ResumePub() {
    super();
  }

  public ResumePub(Long confId, Long pubId) {
    super();
    this.id = new ResumePubKey(confId, pubId);
  }

  @EmbeddedId
  public ResumePubKey getId() {
    return id;
  }

  public void setId(ResumePubKey id) {
    this.id = id;
  }

}
