package com.smate.center.batch.model.pdwh.pub.rainpat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LJ
 *
 *         2017年7月17日
 */
@Entity
@Table(name = "RAINPAT_PUB_EXTEND")
public class RainPatPubExtend implements Serializable {
  private static final long serialVersionUID = -929403078651206871L;
  private Long pubId;
  private String xmlData;

  public RainPatPubExtend() {
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

  @Column(name = "XML_DATA")
  public String getXmlData() {
    return xmlData;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public RainPatPubExtend(Long pubId, String xmlData) {
    super();
    this.pubId = pubId;
    this.xmlData = xmlData;
  }


}
