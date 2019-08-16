package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目分类表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PRJ_CATEGORY")
public class BdspPrjCategory implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * 主键
   */
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_PRJ_CATEGORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  /**
   * 项目id
   */
  @Column(name = "PRJ_ID")
  private Long prjId;
  /**
   * 领域id
   */
  @Column(name = "TECH_ID")
  private String techId;
  /**
   * 领域类型id
   */
  @Column(name = "TECH_TYPE_ID")
  private Long techTypeId;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getTechId() {
    return techId;
  }

  public void setTechId(String techId) {
    this.techId = techId;
  }

  public Long getTechTypeId() {
    return techTypeId;
  }

  public void setTechTypeId(Long techTypeId) {
    this.techTypeId = techTypeId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



}
