package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 第三方部门信息处理历史记录.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "IMPORT_THIRD_UNITS_HISTORY")
public class ImportThirdUnitsHistory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5514204299651817875L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IMPORT_THIRD_UNITS_HISTORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "UNIT_ID")
  private String unitId;// 部门ID
  @Column(name = "INS_ID")
  private Long insId;// 单位ID
  @Column(name = "ZH_NAME")
  private String zhName;// 部门中文名称
  @Column(name = "EN_NAME")
  private String enName;// 部门英文名称
  @Column(name = "SUPER_UNIT_ID")
  private String superUnitId;// 第三方上级部门ID
  @Column(name = "IMPORT_DATE")
  private Date importDate;// 导入日期，对应IMPORT_THIRD_UNITS.CREATE_DATE
  @Column(name = "SIE_UNIT_ID")
  private Long sieUnitId;// SIE部门ID
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期
  @Column(name = "STATUS")
  private Integer status;// 处理状态：0-待处理；1-处理成功；9-处理失败

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(String superUnitId) {
    this.superUnitId = superUnitId;
  }

  public Date getImportDate() {
    return importDate;
  }

  public void setImportDate(Date importDate) {
    this.importDate = importDate;
  }

  public Long getSieUnitId() {
    return sieUnitId;
  }

  public void setSieUnitId(Long sieUnitId) {
    this.sieUnitId = sieUnitId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
