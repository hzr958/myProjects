package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_VALIDATE_DETAIL")
public class KpiValidateDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1980828171064288837L;

  /* 表主键 */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_VALIDATE_DETAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  /* 凭证号 */
  @Column(name = "UUID")
  private String uuId;

  /* 【拓展字段】业务标识 */
  @Column(name = "KEY_CODE")
  private String keyCode;

  /* 验证类型：1人员验证2单位验证3项目成果验证4人员成果验证 */
  @Column(name = "TYPE")
  private Integer type;

  /* 接口地址 */
  @Column(name = "INTERFACE_URL")
  private String interfaceUrl;

  /* 处理状态，0待验证1已验证2多次调用接口异常或失败 */
  @Column(name = "INTERFACE_STATUS")
  private Integer interfaceStatus;

  /* 【机构版专用】1业务验证通过2存疑 */
  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "PARENT_ID")
  private Long parentId;

  /* 接口入参 */
  @Column(name = "PARAMS_IN")
  private String paramsIn;

  /* 验证结果，截取或处理result内容 */
  @Column(name = "PARAMS_OUT")
  private String paramsOut;

  /* 接口返回值 */
  @Column(name = "INTERFACE_RESULT")
  private String interfaceResult;

  public KpiValidateDetail() {
    super();
  }

  public KpiValidateDetail(String uuId, String keyCode, Integer type, String paramsIn, String interfaceUrl,
      Integer interfaceStatus) {
    super();
    this.uuId = uuId;
    this.keyCode = keyCode;
    this.type = type;
    this.paramsIn = paramsIn;
    this.interfaceUrl = interfaceUrl;
    this.interfaceStatus = interfaceStatus;
  }

  public KpiValidateDetail(String uuId, String keyCode, Integer type, String paramsIn, String interfaceUrl,
      Integer interfaceStatus, Long parentId) {
    super();
    this.uuId = uuId;
    this.keyCode = keyCode;
    this.type = type;
    this.paramsIn = paramsIn;
    this.interfaceUrl = interfaceUrl;
    this.interfaceStatus = interfaceStatus;
    this.parentId = parentId;
  }

  public KpiValidateDetail(Long id, String uuId, String keyCode, Integer type, String paramsIn, String interfaceUrl,
      String interfaceResult, String paramsOut, Integer status) {
    super();
    this.id = id;
    this.uuId = uuId;
    this.keyCode = keyCode;
    this.type = type;
    this.paramsIn = paramsIn;
    this.interfaceUrl = interfaceUrl;
    this.interfaceResult = interfaceResult;
    this.paramsOut = paramsOut;
    this.status = status;
  }

  public KpiValidateDetail(Long id, String uuId, String keyCode, Integer type, String interfaceUrl, Integer status,
      String paramsIn, String interfaceResult, String paramsOut) {
    super();
    this.id = id;
    this.uuId = uuId;
    this.keyCode = keyCode;
    this.type = type;
    this.paramsIn = paramsIn;
    this.interfaceUrl = interfaceUrl;
    this.interfaceResult = interfaceResult;
    this.paramsOut = paramsOut;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(String keyCode) {
    this.keyCode = keyCode;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getParamsIn() {
    return paramsIn;
  }

  public void setParamsIn(String paramsIn) {
    this.paramsIn = paramsIn;
  }

  public String getInterfaceUrl() {
    return interfaceUrl;
  }

  public void setInterfaceUrl(String interfaceUrl) {
    this.interfaceUrl = interfaceUrl;
  }

  public String getInterfaceResult() {
    return interfaceResult;
  }

  public void setInterfaceResult(String interfaceResult) {
    this.interfaceResult = interfaceResult;
  }

  public String getParamsOut() {
    return paramsOut;
  }

  public void setParamsOut(String paramsOut) {
    this.paramsOut = paramsOut;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getInterfaceStatus() {
    return interfaceStatus;
  }

  public void setInterfaceStatus(Integer interfaceStatus) {
    this.interfaceStatus = interfaceStatus;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return "KpiValidateDetail [id=" + id + ", uuId=" + uuId + ", keyCode=" + keyCode + ", type=" + type + ", paramsIn="
        + paramsIn + ", interfaceUrl=" + interfaceUrl + ", interfaceResult=" + interfaceResult + ", paramsOut="
        + paramsOut + ", interfaceStatus=" + interfaceStatus + ", status=" + status + "]";
  }
}
