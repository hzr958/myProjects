package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 节点所在个人所有好友.
 * 
 * 
 */

public class NodeIds implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5169592326168364586L;

  private Integer node;
  private List<Long> idList;

  public Integer getNode() {
    return node;
  }

  public void setNode(Integer node) {
    this.node = node;
  }

  public List<Long> getIdList() {
    return idList;
  }

  public void setIdList(List<Long> idList) {
    this.idList = idList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
