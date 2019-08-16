package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目资助类别，自动提示.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_SCHEME")
public class AcPrjScheme implements Serializable {

  private static final long serialVersionUID = 2220256272323981674L;

  // 资助类别ID
  private Long code;
  // 资助类别
  private String name;
  private String enName;
  // 为了检索方便自定义的字符串结构
  private String orderCode;
  // 资助机构ID
  private Long agencyId;

  @Id
  @Column(name = "SCHEME_ID")
  public Long getCode() {
    return code;
  }

  @Column(name = "SCHEME_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "SCHEME_EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "SCHEME_CODE")
  public String getOrderCode() {
    return orderCode;
  }

  @Column(name = "AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

}
