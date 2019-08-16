package com.smate.core.base.utils.model.security;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 个人与单位信息表
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "PSN_INS")
public class PsnIns implements Serializable {

  private static final long serialVersionUID = 8499397731833638543L;
  private PsnInsPk pk;
  private List<Integer> otherRolNodes;
  // 人员状态，0申请加入、1已加入、2单位拒绝、9删除
  private Integer status = 1;
  private Integer nearSnsNodeId;

  public PsnIns() {

    super();
  }

  /**
   * @return pk
   */
  @EmbeddedId
  public PsnInsPk getPk() {
    return pk;
  }

  /**
   * @param pk
   */
  public void setPk(PsnInsPk pk) {
    this.pk = pk;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the otherRolNodes
   */
  @Transient
  public List<Integer> getOtherRolNodes() {
    return otherRolNodes;
  }

  /**
   * @param otherRolNodes the otherRolNodes to set
   */
  public void setOtherRolNodes(List<Integer> otherRolNodes) {
    this.otherRolNodes = otherRolNodes;
  }

  /**
   * @return the nearSnsNodeId
   */
  @Transient
  public Integer getNearSnsNodeId() {
    return nearSnsNodeId;
  }

  /**
   * @param nearSnsNodeId the nearSnsNodeId to set
   */
  public void setNearSnsNodeId(Integer nearSnsNodeId) {
    this.nearSnsNodeId = nearSnsNodeId;
  }

}
