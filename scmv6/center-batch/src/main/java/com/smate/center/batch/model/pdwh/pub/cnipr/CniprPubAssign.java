package com.smate.center.batch.model.pdwh.pub.cnipr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * CNIPR成果地址匹配表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNIPR_PUB_ASSIGN")
public class CniprPubAssign implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7916561717867652398L;
  private Long assignId;
  private Long pubId;
  private Long insId;
  // 0:等待匹配，1已经进行匹配
  private Integer status;
  // 0:未匹配上，1匹配上机构
  private Integer result;
  // 0:未发送到单位，1已经发送到单位
  private Integer isSend;
  private String xmlData;

  public CniprPubAssign() {
    super();
  }

  public CniprPubAssign(Long pubId, Long insId, Integer status, Integer result, Integer isSend) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.status = status;
    this.result = result;
    this.isSend = isSend;
  }

  @Id
  @Column(name = "ASSIGN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNIPR_PUB_ASSIGN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getAssignId() {
    return assignId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "RESULT")
  public Integer getResult() {
    return result;
  }

  @Column(name = "IS_SEND")
  public Integer getIsSend() {
    return isSend;
  }

  @Transient
  public String getXmlData() {
    return xmlData;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public void setAssignId(Long assignId) {
    this.assignId = assignId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setResult(Integer result) {
    this.result = result;
  }

  public void setIsSend(Integer isSend) {
    this.isSend = isSend;
  }

}
