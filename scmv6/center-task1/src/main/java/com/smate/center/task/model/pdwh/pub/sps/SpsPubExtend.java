package com.smate.center.task.model.pdwh.pub.sps;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scopus成果xml数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_PUB_EXTEND")
public class SpsPubExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1067749278302679662L;
  private Long pubId;
  private String xmlData;

  public SpsPubExtend() {
    super();
  }

  public SpsPubExtend(Long pubId, String xmlData) {
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
