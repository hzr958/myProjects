package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * GroupPsnNodeSum复合主键.
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class GroupPsnNodeSumPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3779851730473508185L;

  // [const_dictionary.category=group].code
  private String code;
  // 节点
  private Integer nodeId;

  public GroupPsnNodeSumPk() {

  }

  public GroupPsnNodeSumPk(String code, Integer nodeId) {
    this.code = code;
    this.nodeId = nodeId;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
    GroupPsnNodeSumPk other = (GroupPsnNodeSumPk) obj;
    if (nodeId == null) {
      if (other.nodeId != null)
        return false;
    } else if (!nodeId.equals(other.nodeId))
      return false;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    return true;
  }
}
