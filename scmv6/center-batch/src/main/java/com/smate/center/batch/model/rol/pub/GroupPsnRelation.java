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
 * 用户群组信息
 * 
 * @author zyx
 * 
 */
@Entity
@Table(name = "GROUP_PSN_RELATION")
public class GroupPsnRelation implements Serializable {

  private static final long serialVersionUID = -5860066309067202250L;

  private Long id;
  private Long psnId;
  private Long groupId;
  private Integer nodeId;
  // 部门Id
  private Long unitId;
  // 父部门id
  private Long superUnitId;
  // 单位Id
  private Long insId = -1L;

  public GroupPsnRelation() {
    super();
  }

  public GroupPsnRelation(Long psnId, Long groupId, Integer nodeId) {
    super();
    this.psnId = psnId;
    this.groupId = groupId;
    this.nodeId = nodeId;
  }

  public GroupPsnRelation(Long psnId, Long groupId, Integer nodeId, Long insId, Long unitId, Long superUnitId) {
    this.psnId = psnId;
    this.groupId = groupId;
    this.nodeId = nodeId;
    this.insId = insId;
    this.unitId = unitId;
    this.superUnitId = superUnitId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_PSN_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  @Column(name = "SUPER_UNIT_ID")
  public Long getSuperUnitId() {
    return superUnitId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((superUnitId == null) ? 0 : superUnitId.hashCode());
    result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GroupPsnRelation other = (GroupPsnRelation) obj;
    if (groupId == null) {
      if (other.groupId != null)
        return false;
    } else if (!groupId.equals(other.groupId))
      return false;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (superUnitId == null) {
      if (other.superUnitId != null)
        return false;
    } else if (!superUnitId.equals(other.superUnitId))
      return false;
    if (unitId == null) {
      if (other.unitId != null)
        return false;
    } else if (!unitId.equals(other.unitId))
      return false;
    return true;
  }

}
