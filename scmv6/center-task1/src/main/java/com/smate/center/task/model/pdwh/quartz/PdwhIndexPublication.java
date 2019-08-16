package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_INDEX_PUBLICATION")
public class PdwhIndexPublication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7827112354396871927L;
  private Long pubId;
  private String pubTitle;
  private String shortTitle;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public PdwhIndexPublication() {
    super();
  }

  @Column(name = "PUB_TITLE")
  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  @Column(name = "SHORT_TITLE")
  public String getShortTitle() {
    return shortTitle;
  }

  public void setShortTitle(String shortTitle) {
    this.shortTitle = shortTitle;
  }



  public PdwhIndexPublication(Long pubId, String pubTitle, String shortTitle) {
    super();
    this.pubId = pubId;
    this.pubTitle = pubTitle;
    this.shortTitle = shortTitle;
  }

  public PdwhIndexPublication(Long pubId, String pubTitle) {
    super();
    this.pubId = pubId;
    this.pubTitle = pubTitle;
  }
}
