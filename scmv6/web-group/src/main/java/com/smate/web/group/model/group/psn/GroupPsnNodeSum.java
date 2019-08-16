package com.smate.web.group.model.group.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 群组统计表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_PSN_NODE_SUM")
public class GroupPsnNodeSum implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4932855120887671130L;
  private GroupPsnNodeSumPk id;
  // 统计数量
  private Integer categorySum = 0;

  private String name;

  @EmbeddedId
  public GroupPsnNodeSumPk getId() {
    return id;
  }

  public void setId(GroupPsnNodeSumPk id) {
    this.id = id;
  }

  @Column(name = "CATEGORY_SUM")
  public Integer getCategorySum() {
    return categorySum;
  }

  public void setCategorySum(Integer categorySum) {
    this.categorySum = categorySum;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
