package com.smate.sie.core.base.utils.model.validate.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_KPI_VALIDATE_DETAIL")
public class SieTaskKpiValidateDetail implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5985977253692993463L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID")
  private String uuId;

  @Column(name = "TYPE")
  private Integer type;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "SPLIT_STATUS")
  private Integer splitStatus;

  @Column(name = "PARAMS_IN")
  private String paramsIn;

  @Column(name = "PARAMS_OUT")
  private String paramsOut;

  @Column(name = "INTERFACE_RESULT")
  private String interfaceResult;

  public SieTaskKpiValidateDetail() {
    super();
  }

  public SieTaskKpiValidateDetail(Long id, String uuId, Integer type, Integer status, Integer splitStatus,
      String paramsIn, String paramsOut, String interfaceResult) {
    super();
    this.id = id;
    this.uuId = uuId;
    this.type = type;
    this.status = status;
    this.splitStatus = splitStatus;
    this.paramsIn = paramsIn;
    this.paramsOut = paramsOut;
    this.interfaceResult = interfaceResult;
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

  public String getInterfaceResult() {
    return interfaceResult;
  }

  public void setInterfaceResult(String interfaceResult) {
    this.interfaceResult = interfaceResult;
  }

  public Integer getSplitStatus() {
    return splitStatus;
  }

  public void setSplitStatus(Integer splitStatus) {
    this.splitStatus = splitStatus;
  }

  @Override
  public String toString() {
    return "TmpKpiValidateDetail [id=" + id + ", uuId=" + uuId + ", type=" + type + ", paramsIn=" + paramsIn
        + ", paramsOut=" + paramsOut + ", status=" + status + ", interfaceResult=" + interfaceResult + ", splitStatus="
        + splitStatus + "]";
  }
}
