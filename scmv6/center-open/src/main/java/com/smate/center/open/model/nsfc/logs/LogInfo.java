package com.smate.center.open.model.nsfc.logs;

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
 * 日志model类
 * 
 * @author tsz
 * 
 */

@Entity
@Table(name = "LOG_INFO")
public class LogInfo implements Serializable {

  private static final long serialVersionUID = -6194016972911880898L;

  @Id
  @SequenceGenerator(name = "LOG_INFO_GENERATOR", sequenceName = "SEQ_LOG_INFO")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_INFO_GENERATOR")
  private Long id;

  // 客户IP
  @Column(name = "CLIENT_IP")
  private String clientIP;

  // 操作类型（参考SyncTypeEnum.java）
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 调用参数（多个参数，换行隔开）
  @Column(name = "PARAMETERS")
  private String parameters;

  // 调用方法名
  @Column(name = "METHOD_NAME")
  private String methodName;

  // 操作状态（1：成功，2：失败）
  @Column(name = "STATUS")
  private Integer status;

  // 操作结果（也有可能是异常信息）
  @Column(name = "ACTION_RESOURCE")
  private String actionResource;

  // 操作时间
  @Column(name = "ACTION_DATE")
  private Date actionDate;

  // 说明
  @Column(name = "DESCRIPTION")
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClientIP() {
    return clientIP;
  }

  public void setClientIP(String clientIP) {
    this.clientIP = clientIP;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getActionResource() {
    return actionResource;
  }

  public void setActionResource(String actionResource) {
    this.actionResource = actionResource;
  }

  public Date getActionDate() {
    return actionDate;
  }

  public void setActionDate(Date actionDate) {
    this.actionDate = actionDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
