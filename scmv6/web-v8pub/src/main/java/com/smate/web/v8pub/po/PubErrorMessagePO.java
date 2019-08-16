package com.smate.web.v8pub.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果错误信息记录表 sns库与pdwh的错误信息都存储在此表中
 * 
 * @author YJ
 *
 *         2018年8月7日
 */

@Entity
@Table(name = "V_PUB_ERROR_MESSAGE")
public class PubErrorMessagePO implements Serializable {

  private static final long serialVersionUID = -1253699662201272037L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ERROR_MESSAGE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "TYPE")
  private Integer type; // 0 为sns库成果，1 为pdwh库成果

  @Column(name = "HANDLER_NAME")
  private String handlerName; // 处理器名

  @Column(name = "OPERATOR_ID")
  private Long operatorId;// 当前操作者psnId，如果是任务无操作者，设置为0L

  @Column(name = "ERROR_DATE")
  private Date errorDate; // 错误产生的时间

  @Column(name = "ERROR_INFO")
  private String errorInfo; // 具体的错误信息

  public PubErrorMessagePO() {
    super();
  }

  public PubErrorMessagePO(Long pubId, Integer type, String handlerName, Long operatorId, String errorInfo) {
    this.pubId = pubId;
    this.type = type;
    this.handlerName = handlerName;
    this.operatorId = operatorId;
    this.errorInfo = errorInfo;
    this.errorDate = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  public Date getErrorDate() {
    return errorDate;
  }

  public void setErrorDate(Date errorDate) {
    this.errorDate = errorDate;
  }

  public String getErrorInfo() {
    return errorInfo;
  }

  public void setErrorInfo(String errorInfo) {
    this.errorInfo = errorInfo;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getHandlerName() {
    return handlerName;
  }

  public void setHandlerName(String handlerName) {
    this.handlerName = handlerName;
  }


}
