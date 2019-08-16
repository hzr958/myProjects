package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table(name = "PUB_TF")
public class PubTf implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6298174348351735024L;
  private Long pubId;
  private String tfDataZh;
  private String tfDataEn;

  public PubTf() {
    super();
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "TF_ZH")
  public String getTfDataZh() {
    return tfDataZh;
  }

  @Column(name = "TF_EN")
  public String getTfDataEn() {
    return tfDataEn;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setTfDataZh(String tfDataZh) {
    this.tfDataZh = tfDataZh;
  }

  public void setTfDataEn(String tfDataEn) {
    this.tfDataEn = tfDataEn;
  }

}
