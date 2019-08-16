package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果XML.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ROL_PUB_XML")
public class RolPubXml implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -6895183037241822603L;
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
