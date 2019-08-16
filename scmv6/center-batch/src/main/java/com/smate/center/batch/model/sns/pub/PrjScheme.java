package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_SCHEME")
public class PrjScheme implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2220256272323981674L;

  // 资助类别ID
  private Long id;
  // 资助类别中文名
  private String name;
  // 资助类别外文名
  private String enName;
  // 为了检索方便自定义的字符串结构
  private String code;
  // 资助机构ID
  private Long agencyId;

  @Id
  @Column(name = "SCHEME_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "SCHEME_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "SCHEME_CODE")
  public String getCode() {
    return code;
  }

  @Column(name = "SCHEME_EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

}
