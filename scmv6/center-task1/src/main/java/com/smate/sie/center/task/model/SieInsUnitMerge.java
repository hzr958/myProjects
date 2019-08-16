package com.smate.sie.center.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 部门合并记录
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "MERGE_UNIT")
public class SieInsUnitMerge {

  // 主键，id
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MERGE_UNIT", allocationSize = 1)
  private Long id;

  // 单位主键
  @Column(name = "INS_ID")
  private Long insId;

  // 被合并部门
  @Column(name = "UNIT_A")
  private String unitA;

  // 合并至部门
  @Column(name = "UNIT_B")
  private String unitB;

  // 被合并部门人数
  @Column(name = "PSN_NUM")
  private Long psnNum;

  // 合并时间
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 被合并部门ID
  @Column(name = "UNIT_A_ID")
  private Long unitAId;

  // 合并至部门ID
  @Column(name = "UNIT_B_ID")
  private Long unitBId;

  public SieInsUnitMerge() {
    super();
  }

  public SieInsUnitMerge(Long id, Long insId, String unitA, String unitB, Long psnNum, Date createDate) {
    super();
    this.id = id;
    this.insId = insId;
    this.unitA = unitA;
    this.unitB = unitB;
    this.psnNum = psnNum;
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getUnitA() {
    return unitA;
  }

  public void setUnitA(String unitA) {
    this.unitA = unitA;
  }

  public String getUnitB() {
    return unitB;
  }

  public void setUnitB(String unitB) {
    this.unitB = unitB;
  }

  public Long getPsnNum() {
    return psnNum;
  }

  public void setPsnNum(Long psnNum) {
    this.psnNum = psnNum;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getUnitAId() {
    return unitAId;
  }

  public void setUnitAId(Long unitAId) {
    this.unitAId = unitAId;
  }

  public Long getUnitBId() {
    return unitBId;
  }

  public void setUnitBId(Long unitBId) {
    this.unitBId = unitBId;
  }

}
