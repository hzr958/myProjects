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
 * open系统 接口 请求处理日志
 * 
 * 只记录 正确处理的日志 统计
 * 
 * 如果需要统计所有的 需要加上错误日志表的记录
 * 
 * @author tsz
 *
 */

@Entity
@Table(name = "V_OPEN_DATA_HANDLE_LOG")
public class OpenDataHandleLog {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_DATA_HANDLE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; //
  @Column(name = "TOKEN")
  private String token; // 系统授权码
  @Column(name = "SERVICE_TYPE")
  private String serviceType; // 服务编码
  @Column(name = "SUM")
  private Integer sum; // 统计记录
  @Column(name = "LAST_ACCESS")
  private Date lastAccess; // 最后访问时间
  @Column(name = "DISC")
  private String disc; // 描述

  public OpenDataHandleLog() {
    super();
  }

  public OpenDataHandleLog(Long id, String token, String serviceType, Integer sum, Date lastAccess, String disc) {
    super();
    this.id = id;
    this.token = token;
    this.serviceType = serviceType;
    this.sum = sum;
    this.lastAccess = lastAccess;
    this.disc = disc;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getSum() {
    return sum;
  }

  public void setSum(Integer sum) {
    this.sum = sum;
  }

  public Date getLastAccess() {
    return lastAccess;
  }

  public void setLastAccess(Date lastAccess) {
    this.lastAccess = lastAccess;
  }

  public String getDisc() {
    return disc;
  }

  public void setDisc(String disc) {
    this.disc = disc;
  }

}
