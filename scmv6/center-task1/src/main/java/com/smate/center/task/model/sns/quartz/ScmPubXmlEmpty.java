package com.smate.center.task.model.sns.quartz;

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
@Table(name = "SCM_PUB_XML_EMPTY")
public class ScmPubXmlEmpty implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1028747895695322706L;

  private Long pubId;

  public ScmPubXmlEmpty() {
    super();
  }

  public ScmPubXmlEmpty(Long pubId) {
    super();
    this.pubId = pubId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
