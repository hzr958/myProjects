package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * isi成果xml数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_PUB_EXTEND")
public class IsiPubExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5137693139321030617L;
  private Long pubId;
  private String xmlData;

  public IsiPubExtend() {
    super();
  }

  public IsiPubExtend(Long pubId, String xmlData) {
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
