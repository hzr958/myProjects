package com.smate.center.open.model.data.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * open系统 接口 访问日志表
 * 
 * 只记录访问日志
 * 
 * @author tsz
 *
 */

@Entity
@Table(name = "V_OPEN_DATA_ACCESS_LOG")
public class OpenDataAccessLog {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_DATA_ACCESS_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; //
  @Column(name = "OPEN_ID")
  private String openId; // 系统授权码
  @Column(name = "TOKEN")
  private String token; // 系统授权码
  @Column(name = "SERVICE_TYPE")
  private String serviceType; // 服务编码
  @Column(name = "reqeust_type")
  private String requestType; // 请求类型 1 webservice or 2 restful, 3 内部调用
  @Column(name = "parameter")
  private String parameter; // 参数
  @Column(name = "ACCESS_DATE")
  private Date accessDate; // 访问时间
  @Column(name = "DISC")
  private String disc; // 描述

  public OpenDataAccessLog() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getDisc() {
    return disc;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public void setDisc(String disc) {
    this.disc = disc;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public Date getAccessDate() {
    return accessDate;
  }

  public void setAccessDate(Date accessDate) {
    this.accessDate = accessDate;
  }

}
