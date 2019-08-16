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
 * 专利分类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PATENT_CATEGORY")
public class BdspPatentCategory implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_PATENT_CATEGORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
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

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
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

}
