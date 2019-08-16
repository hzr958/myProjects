package com.smate.center.batch.model.pdwh.pub.oalib;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * oalib 成果xml数据表
 * 
 * @author LIJUN
 *
 */
@Entity
@Table(name = "OALIB_PUB_EXTEND")
public class OalibPubExtend implements Serializable {

  private static final long serialVersionUID = 3897700371780203104L;
  private Long pubId;
  private String xmlData;

  public OalibPubExtend() {
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

  public OalibPubExtend(Long pubId, String xmlData) {
    super();
    this.pubId = pubId;
    this.xmlData = xmlData;
  }

}
