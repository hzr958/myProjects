package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = " CROSSREF_OTHER_INFO")
public class CrossrefOtherInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 204780121715679111L;

  private Long pubId;
  private Long crossrefMemberId;
  private String depositedDate;
  private String fulltextUrls;
  private String eissn;


  public CrossrefOtherInfo() {
    super();
  }

  public CrossrefOtherInfo(Long pubId) {
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

  @Column(name = "CROSSREF_MEMBER_ID")
  public Long getCrossrefMemberId() {
    return crossrefMemberId;
  }

  public void setCrossrefMemberId(Long crossrefMemberId) {
    this.crossrefMemberId = crossrefMemberId;
  }

  @Column(name = "DEPOSITED_DATE")
  public String getDepositedDate() {
    return depositedDate;
  }

  public void setDepositedDate(String depositedDate) {
    this.depositedDate = depositedDate;
  }

  @Column(name = "FULLTEXT_URLS")
  public String getFulltextUrls() {
    return fulltextUrls;
  }

  public void setFulltextUrls(String fulltextUrls) {
    this.fulltextUrls = fulltextUrls;
  }

  @Column(name = "EISSN")
  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }


}
