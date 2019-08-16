package com.smate.center.open.model.pdwh.pub;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果xml表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_XML")
public class PdwhPubXml implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1752849433382859501L;
  private Long pubId;
  private String xml;

  public PdwhPubXml() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }


  @Column(name = "XML")
  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }



}
