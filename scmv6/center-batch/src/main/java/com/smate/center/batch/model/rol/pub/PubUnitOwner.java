package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果部门创建关系表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_UNIT_OWNER")
public class PubUnitOwner implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1127356130155541905L;

  // 成果ID
  private Long pubId;
  // 单位ID
  private Long insId;
  // 部门ID
  private Long unitId;
  // 上级部门ID
  private Long parentUnitId;

  public PubUnitOwner() {
    super();
  }

  public PubUnitOwner(Long pubId, Long insId, Long unitId, Long parentUnitId) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.unitId = unitId;
    this.parentUnitId = parentUnitId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  @Column(name = "PARENT_UNIT_ID")
  public Long getParentUnitId() {
    return parentUnitId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setParentUnitId(Long parentUnitId) {
    this.parentUnitId = parentUnitId;
  }

}
