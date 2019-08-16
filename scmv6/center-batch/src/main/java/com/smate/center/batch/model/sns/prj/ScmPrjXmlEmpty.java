package com.smate.center.batch.model.sns.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果XML为空的记录.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SCM_PRJ_XML_EMPTY")
public class ScmPrjXmlEmpty implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1192548951658890745L;
  private Long prjId;

  public ScmPrjXmlEmpty() {
    super();
  }

  public ScmPrjXmlEmpty(Long prjId) {
    super();
    this.prjId = prjId;
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

}
