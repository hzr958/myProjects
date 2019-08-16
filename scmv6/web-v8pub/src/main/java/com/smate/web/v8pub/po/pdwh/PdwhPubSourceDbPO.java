package com.smate.web.v8pub.po.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果dbid表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_SOURCE_DB")
public class PdwhPubSourceDbPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6814296072793652315L;
  private Long pubId;
  private Integer ei = 0;
  private Integer sci = 0;
  private Integer istp = 0;
  private Integer ssci = 0;
  private Integer cnki = 0;
  private Integer cnkiPat = 0;
  private Integer rainPat = 0;

  public PdwhPubSourceDbPO() {
    super();
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "EI")
  public Integer getEi() {
    return ei;
  }

  public void setEi(Integer ei) {
    this.ei = ei;
  }

  @Column(name = "SCI")
  public Integer getSci() {
    return sci;
  }

  public void setSci(Integer sci) {
    this.sci = sci;
  }

  @Column(name = "ISTP")
  public Integer getIstp() {
    return istp;
  }

  public void setIstp(Integer istp) {
    this.istp = istp;
  }

  @Column(name = "SSCI")
  public Integer getSsci() {
    return ssci;
  }

  public void setSsci(Integer ssci) {
    this.ssci = ssci;
  }

  @Column(name = "CNKI")
  public Integer getCnki() {
    return cnki;
  }

  public void setCnki(Integer cnki) {
    this.cnki = cnki;
  }

  @Column(name = "CNKIPAT")
  public Integer getCnkiPat() {
    return cnkiPat;
  }

  public void setCnkiPat(Integer cnkiPat) {
    this.cnkiPat = cnkiPat;
  }

  @Column(name = "RAINPAT")
  public Integer getRainPat() {
    return rainPat;
  }

  public void setRainPat(Integer rainPat) {
    this.rainPat = rainPat;
  }

}
