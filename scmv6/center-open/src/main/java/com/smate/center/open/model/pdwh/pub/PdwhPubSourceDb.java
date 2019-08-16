package com.smate.center.open.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果收录表
 * 
 * @author ajb
 *
 */
@Entity
@Table(name = "PDWH_PUB_SOURCE_DB")
public class PdwhPubSourceDb implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5683289440990837492L;

  private Long pubId;
  private Integer ei;
  private Integer sci;
  private Integer istp;
  private Integer ssci;
  private Integer cnki;
  private Integer cnkipat;
  private Integer rainpat;

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
  public Integer getCnkipat() {
    return cnkipat;
  }

  public void setCnkipat(Integer cnkipat) {
    this.cnkipat = cnkipat;
  }

  @Column(name = "RAINPAT")
  public Integer getRainpat() {
    return rainpat;
  }

  public void setRainpat(Integer rainpat) {
    this.rainpat = rainpat;
  }

  public PdwhPubSourceDb() {
    super();
  }

}
