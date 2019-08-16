package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PSN_COPARTNER")
public class PsnCopartner implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8617034438639337530L;
  private Long id;
  private Long psnId;
  private Long coPsnId;
  private Long pdwhPubId;
  private Long grpId;
  private String pdwhPubName;
  private Long pdwhPubMemberId;
  private Integer coType;

  public PsnCopartner() {
    super();
  }

  public PsnCopartner(Long psnId, Long coPsnId, Long grpId, Integer coType) {
    super();
    this.psnId = psnId;
    this.coPsnId = coPsnId;
    this.grpId = grpId;
    this.coType = coType;
  }

  public PsnCopartner(Long psnId, Long pdwhPubId, Integer coType) {
    super();
    this.psnId = psnId;
    this.pdwhPubId = pdwhPubId;
    this.coType = coType;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_COPARTNER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CO_PSN_ID")
  public Long getCoPsnId() {
    return coPsnId;
  }

  public void setCoPsnId(Long coPsnId) {
    this.coPsnId = coPsnId;
  }

  @Column(name = "GRP_ID")
  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "PDWH_PUB_NAME")
  public String getPdwhPubName() {
    return pdwhPubName;
  }

  public void setPdwhPubName(String pdwhPubName) {
    this.pdwhPubName = pdwhPubName;
  }

  @Column(name = "PDWH_PUB_MEMBER_ID")
  public Long getPdwhPubMemberId() {
    return pdwhPubMemberId;
  }

  public void setPdwhPubMemberId(Long pdwhPubMemberId) {
    this.pdwhPubMemberId = pdwhPubMemberId;
  }

  @Column(name = "CO_TYPE")
  public Integer getCoType() {
    return coType;
  }

  public void setCoType(Integer coType) {
    this.coType = coType;
  }

}
