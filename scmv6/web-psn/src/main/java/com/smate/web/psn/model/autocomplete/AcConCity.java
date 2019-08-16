package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 城市自动提示.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "CONST_CITY_AUTO")
public class AcConCity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6655182709619595791L;
  private Long id;
  private Long cyId;
  private String name;
  private String cnName;
  private String enName;
  private Long prvId;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
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

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

}
