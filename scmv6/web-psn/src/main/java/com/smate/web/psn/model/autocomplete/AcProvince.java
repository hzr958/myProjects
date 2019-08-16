package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 省份.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "CONST_PROVINCE_AUTO")
public class AcProvince implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5398099136586754582L;
  private Long id;
  private Long prvId;
  private String name;
  private String cnName;
  private String enName;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  @Column(name = "AUTO_NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "ZH_NAME")
  public String getCnName() {
    return cnName;
  }

  public void setCnName(String cnName) {
    this.cnName = cnName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }
}
