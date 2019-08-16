package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果xml
 * 
 * @author ztt
 */
@Entity
@Table(name = "PUB_XML")
public class SiePubXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8071654336738481535L;

  private Long pubId;
  private String pubXml;

  public SiePubXml() {
    super();
  }

  public SiePubXml(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public SiePubXml(Long pubId, String pubXml) {
    super();
    this.pubId = pubId;
    this.pubXml = pubXml;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_XML")
  public String getPubXml() {
    return pubXml;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

}
