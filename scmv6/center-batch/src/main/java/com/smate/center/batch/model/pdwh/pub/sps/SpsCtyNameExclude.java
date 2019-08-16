package com.smate.center.batch.model.pdwh.pub.sps;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scopus匹配排除国外名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_CTY_NAME_EXCLUDE")
public class SpsCtyNameExclude implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7484435330712472771L;
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
