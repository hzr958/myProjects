package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果匹配，单位国家包含的.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_REGION_INCLUDE")
public class PubRegionInclude implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8572339945466280395L;
  private Long id;
  private Long insId;
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

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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
