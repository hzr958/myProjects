package com.smate.center.open.model.sie.publication;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位成果XML.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "ROL_PUB_XML")
public class RolPubXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -327274310561063853L;
  private Long pubId;
  private String pubXml;

  public RolPubXml() {
    super();
  }

  public RolPubXml(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public RolPubXml(Long pubId, String pubXml) {
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
