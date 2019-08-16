package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目资助机构自动提示.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_SCHEME_AGENCY")
public class AcPrjSchemeAgency implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8736478818236176946L;

  // 项目资助机构ID
  private Long code;
  // 项目资助机构
  private String name;
  private String enName;
  // 为了检索方便自定义的字符串结构
  private String orderCode;

  @Id
  @Column(name = "AGENCY_ID")
  public Long getCode() {
    return code;
  }

  @Column(name = "AGENCY_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "AGENCY_CODE")
  public String getOrderCode() {
    return orderCode;
  }

  @Column(name = "AGENCY_EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
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

}
