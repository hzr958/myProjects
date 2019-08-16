package com.smate.center.task.model.pdwh.quartz;

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
 * 基准库成果匹配表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_ASSIGN")
public class PdwhPubAssign implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 7688028613883775979L;
  private Long assignId;
  private Long pubId;
  private Long insId;
  private Integer status;// 匹配状态(0:等待匹配，1已经进行匹配)
  private Integer result;// 匹配结果(0:未匹配上，1匹配上机构，2匹配上其他机构，3部分匹配上，4匹配上其他机构或者地址为空)
  private Integer isSend;// 发送状态(0:未发送到单位，1已经发送到单位)
  private String xmlString;

  public PdwhPubAssign() {
    super();
  }

  public PdwhPubAssign(Long pubId, Long insId, Integer status, Integer result, Integer isSend) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.status = status;
    this.result = result;
    this.isSend = isSend;
  }

  @Id
  @Column(name = "ASSIGN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_APDWH_PUB_ASSIGN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getAssignId() {
    return assignId;
  }

  public void setAssignId(Long assignId) {
    this.assignId = assignId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "RESULT")
  public Integer getResult() {
    return result;
  }

  public void setResult(Integer result) {
    this.result = result;
  }

  @Column(name = "IS_SEND")
  public Integer getIsSend() {
    return isSend;
  }

  public void setIsSend(Integer isSend) {
    this.isSend = isSend;
  }

  @Transient
  public String getXmlString() {
    return xmlString;
  }

  public void setXmlString(String xmlString) {
    this.xmlString = xmlString;
  }

}
