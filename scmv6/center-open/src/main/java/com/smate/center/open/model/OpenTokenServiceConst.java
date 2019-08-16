package com.smate.center.open.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * TOKEN 和 service_type 对应的常量表
 * 
 * @author AiJiangBin
 *
 */

@Entity
@Table(name = "V_OPEN_TOKEN_SERVICE_CONST")
public class OpenTokenServiceConst implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4857177592032762721L;

  @Id
  @Column(name = "ID")
  private Long id; // 主键 ，数据库自增

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "SERVICE_TYPE")
  private String serviceType;

  @Column(name = "STATUS")
  private Integer status; // 状态0 正常使用， 1无效

  @Column(name = "CREATE_DATE")
  private Date crater_date; // 创建日期

  @Column(name = "DESCR")
  private String descr; // 描述


  @Column(name = "ACCESS_DATE")
  private Date accessDate; // 访问时间日期

  @Column(name = "ACCESS_NUM")
  private Integer accessNum; // 访问次数

  @Column(name = "ACCESS_MAX_NUM")
  private Integer accessMaxNum; // 访问的最大次数
  @Column(name = "INS_ID")
  private Long insId; // 限制单位id访问


  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCrater_date() {
    return crater_date;
  }

  public void setCrater_date(Date crater_date) {
    this.crater_date = crater_date;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public Date getAccessDate() {
    return accessDate;
  }

  public void setAccessDate(Date accessDate) {
    this.accessDate = accessDate;
  }

  public Integer getAccessNum() {
    return accessNum;
  }

  public void setAccessNum(Integer accessNum) {
    this.accessNum = accessNum;
  }

  public Integer getAccessMaxNum() {
    return accessMaxNum;
  }

  public void setAccessMaxNum(Integer accessMaxNum) {
    this.accessMaxNum = accessMaxNum;
  }


}
