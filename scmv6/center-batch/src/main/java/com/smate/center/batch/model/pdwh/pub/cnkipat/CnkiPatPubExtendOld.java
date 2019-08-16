package com.smate.center.batch.model.pdwh.pub.cnkipat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CNKI专利成果xml数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKIPAT_PUB_EXTEND_OLD")
public class CnkiPatPubExtendOld implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1813413239655039381L;
  private Long pubId;
  private String xmlData;

  public CnkiPatPubExtendOld() {
    super();
  }

  public CnkiPatPubExtendOld(Long pubId, String xmlData) {
    super();
    this.pubId = pubId;
    this.xmlData = xmlData;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "XML_DATA")
  public String getXmlData() {
    return xmlData;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

}
