package com.smate.web.psn.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果ID和SNS成果ID关系对应表
 * 
 * @author LJ
 *
 */

@Entity
@Table(name = "PUB_PDWH_SNS_RELATION")
public class PubPdwhSnsRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 210577926013580991L;
  private Long snsPubId;
  private Long pdwhPubId;

  public PubPdwhSnsRelation() {
    super();
  }

  public PubPdwhSnsRelation(Long snsPubId, Long pdwhPubId) {
    super();
    this.snsPubId = snsPubId;
    this.pdwhPubId = pdwhPubId;
  }

  @Id
  @Column(name = "SNS_PUB_ID")
  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }



}
