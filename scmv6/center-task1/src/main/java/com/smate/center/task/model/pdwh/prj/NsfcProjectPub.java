package com.smate.center.task.model.pdwh.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NSFC_PROJECT_PUB")
public class NsfcProjectPub implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 6017515079413865282L;

  private Long prjId;
  private String prjYear;
  private String nsfcCategory;
  private String snsPubId;
  private Long pdwhPubId;


  public NsfcProjectPub() {
    super();
  }

  public NsfcProjectPub(Long prjId, String prjYear, String nsfcCategory, Long pdwhPubId) {
    super();
    this.prjId = prjId;
    this.prjYear = prjYear;
    this.nsfcCategory = nsfcCategory;
    this.pdwhPubId = pdwhPubId;
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }


  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "PRJ_YEAR")
  public String getPrjYear() {
    return prjYear;
  }


  public void setPrjYear(String prjYear) {
    this.prjYear = prjYear;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategory() {
    return nsfcCategory;
  }


  public void setNsfcCategory(String nsfcCategory) {
    this.nsfcCategory = nsfcCategory;
  }

  @Column(name = "SNS_PUB_ID")
  public String getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(String snsPubId) {
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
