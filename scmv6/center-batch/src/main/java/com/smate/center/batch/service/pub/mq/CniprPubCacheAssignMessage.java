package com.smate.center.batch.service.pub.mq;

/**
 * Cnipr抓取成果XML指派给单位.
 * 
 * @author liqinghua
 * 
 */
public class CniprPubCacheAssignMessage {

  private Long xmlId;
  private String xmlData;
  private Long insId;

  public CniprPubCacheAssignMessage() {
    super();
  }

  public CniprPubCacheAssignMessage(Long xmlId, String xmlData, Long insId) {
    super();
    this.xmlId = xmlId;
    this.xmlData = xmlData;
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

}
