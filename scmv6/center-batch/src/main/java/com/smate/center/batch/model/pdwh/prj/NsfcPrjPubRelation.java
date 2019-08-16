package com.smate.center.batch.model.pdwh.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NSFC_PROJECT_PUB")
public class NsfcPrjPubRelation implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 6017515079413865282L;

  private Long id;
  private Long prjId;
  private Long scmPubId;

  public NsfcPrjPubRelation() {
    super();
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "PUB_ID")
  public Long getScmPubId() {
    return scmPubId;
  }

  public void setScmPubId(Long scmPubId) {
    this.scmPubId = scmPubId;
  }

}
