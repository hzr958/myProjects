package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 领域常量表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_TECH_TYPE")
public class BdspTechType implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 
   */
  @Id
  @Column(name = "TECH_ID")
  private Long techId;
  @Column(name = "TECH_NAME")
  private String techName;

  public Long getTechId() {
    return techId;
  }

  public void setTechId(Long techId) {
    this.techId = techId;
  }

  public String getTechName() {
    return techName;
  }

  public void setTechName(String techName) {
    this.techName = techName;
  }



}
