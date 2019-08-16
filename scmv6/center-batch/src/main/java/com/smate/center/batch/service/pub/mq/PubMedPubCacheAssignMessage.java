package com.smate.center.batch.service.pub.mq;

/**
 * PUBMED抓取成果XML指派给单位.
 * 
 * @author liqinghua
 * 
 */
public class PubMedPubCacheAssignMessage {

  private Integer type = 1;
  private Long xmlId;
  private String xmlData;
  private Long insId;

  public PubMedPubCacheAssignMessage() {
    super();
  }

  public PubMedPubCacheAssignMessage(Integer type, Long xmlId, String xmlData, Long insId) {
    super();
    this.type = type;
    this.xmlId = xmlId;
    this.xmlData = xmlData;
    this.insId = insId;
  }

  public PubMedPubCacheAssignMessage(Integer type, Long xmlId, Long insId) {
    super();
    this.type = type;
    this.xmlId = xmlId;
    this.insId = insId;
  }

  public Long getXmlId() {
    return xmlId;
  }

  public String getXmlData() {
    return xmlData;
  }

  public Long getInsId() {
    return insId;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
