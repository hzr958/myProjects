package com.smate.center.batch.model.pdwh.pub.ei;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ei成果xml数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "EI_PUB_EXTEND")
public class EiPubExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -419459367400005882L;
  private Long pubId;
  private String xmlData;

  public EiPubExtend() {
    super();
  }

  public EiPubExtend(Long pubId, String xmlData) {
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
