package com.smate.center.open.isis.model.data.isis;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the NSFC_PRJ_PUB_REPORT database table.
 * 
 */

public class NsfcPrjPubReportPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6106092303709309204L;

  public NsfcPrjPubReportPK() {
    super();
  }

  public NsfcPrjPubReportPK(Long rptId, Long prjId, Long pubId) {
    this.rptId = rptId;
    this.prjId = prjId;
    this.pubId = pubId;
  }

  @Column(name = "RPT_ID")
  private Long rptId;
  @Column(name = "PRJ_ID")
  private Long prjId;

  @Column(name = "PUB_ID")
  private Long pubId;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getRptId() {
    return rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }


}
