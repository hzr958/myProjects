package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ISI匹配排除国外、或地区名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_REGION_EXCLUDE")
public class PubRegionExclude implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8827151140208974206L;

  private Long id;
  // 国家或地区名
  private String name;
  // 国家或地区ID
  private Long regionId;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

}
