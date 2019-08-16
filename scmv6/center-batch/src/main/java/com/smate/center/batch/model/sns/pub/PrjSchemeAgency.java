package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目资助机构.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_SCHEME_AGENCY")
public class PrjSchemeAgency implements Serializable {
  private static final long serialVersionUID = -8736478818236176946L;

  // 项目资助机构ID
  private Long id;
  // 项目资助机构中文名
  private String name;
  // 项目资助机构外文名
  private String enName;
  // 为了检索方便自定义的字符串结构
  private String code;

  public PrjSchemeAgency() {
    super();
  }

  public PrjSchemeAgency(Long id, String name, String enName, String code) {
    super();
    this.id = id;
    this.name = name;
    this.enName = enName;
    this.code = code;
  }

  @Id
  @Column(name = "AGENCY_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "AGENCY_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "AGENCY_EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "AGENCY_CODE")
  public String getCode() {
    return code;
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

}
