package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 动态扩展信息model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "DYNAMIC_EXTEND")
public class DynamicExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5737254653671941750L;
  private Long dynExtId;
  private Long dynId;
  private int objectType;
  private Long objectId;
  private int objectNode;
  private int firstFlag;

  @Id
  @Column(name = "DYN_EXT_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_EXTEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getDynExtId() {
    return dynExtId;
  }

  public void setDynExtId(Long dynExtId) {
    this.dynExtId = dynExtId;
  }

  @Column(name = "DYN_ID")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  @Column(name = "OBJECT_TYPE")
  public int getObjectType() {
    return objectType;
  }

  public void setObjectType(int objectType) {
    this.objectType = objectType;
  }

  @Column(name = "OBJECT_ID")
  public Long getObjectId() {
    return objectId;
  }

  public void setObjectId(Long objectId) {
    this.objectId = objectId;
  }

  @Column(name = "OBJECT_NODE")
  public int getObjectNode() {
    return objectNode;
  }

  public void setObjectNode(int objectNode) {
    this.objectNode = objectNode;
  }

  @Column(name = "FIRST_FLAG")
  public int getFirstFlag() {
    return firstFlag;
  }

  public void setFirstFlag(int firstFlag) {
    this.firstFlag = firstFlag;
  }
}
