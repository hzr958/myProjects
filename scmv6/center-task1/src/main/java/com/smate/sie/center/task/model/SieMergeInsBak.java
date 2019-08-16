package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 记录合并单位信息操作日志表
 * 
 * @author 叶星源
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "MERGE_INS")
public class SieMergeInsBak implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MERGE_INS_BAK", allocationSize = 1)
  @Column(name = "id")
  private Integer id; // 表主键
  @Column(name = "BUSINESS_ID")
  private Long businessId;
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "TYPE_ID")
  private Integer typeId;

  public Long getBusinessId() {
    return businessId;
  }

  public Long getInsId() {
    return insId;
  }

  public Integer getId() {
    return id;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setBusinessId(Long businessId) {
    this.businessId = businessId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public SieMergeInsBak(Long businessId, Long insId, Integer typeId) {
    super();
    this.businessId = businessId;
    this.insId = insId;
    this.typeId = typeId;
  }

  public SieMergeInsBak() {
    super();
  }

}
