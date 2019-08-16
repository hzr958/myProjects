package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目部门创建关系表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PRJ_UNIT_OWNER")
public class RolPrjUnitOwner implements Serializable {

  private static final long serialVersionUID = -9027982751425931883L;
  private Long id;// 主键.
  private Long prjId;// 项目ID.
  private Long unitId;// 部门ID.
  private Long superUnitId;// 上级部门ID.
  private Long insId;// 单位ID.

  public RolPrjUnitOwner() {
    super();
  }

  public RolPrjUnitOwner(Long id, Long prjId, Long unitId, Long superUnitId, Long insId) {
    super();
    this.id = id;
    this.prjId = prjId;
    this.unitId = unitId;
    this.superUnitId = superUnitId;
    this.insId = insId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_UNIT_OWNER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Column(name = "SUPER_UNIT_ID")
  public Long getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
