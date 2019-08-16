package com.smate.center.open.model.sie.publication;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果ID和ROL成果ID关系对应表
 * 
 * @author AJB
 *
 */

@Entity
@Table(name = "PUB_PDWH_ROL_RELATION")
public class PubPdwhRolRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 210577926013580991L;
  private Long rolPubId;
  private Long pdwhPubId;

  public PubPdwhRolRelation() {
    super();
  }

  public PubPdwhRolRelation(Long rolPubId, Long pdwhPubId) {
    super();
    this.rolPubId = rolPubId;
    this.pdwhPubId = pdwhPubId;
  }

  @Id
  @Column(name = "ROL_PUB_ID")
  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

}
