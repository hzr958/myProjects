package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 统计群组好友.
 * 
 * @author zhuangyanming
 * 
 */
public class GroupPsnFriendSum implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6704273899311847821L;

  // 统计数量
  private Integer categorySum = 0;
  // [const_dictionary.category=group].code
  private String code;
  // 群组类别名
  private String name;

  public Integer getCategorySum() {
    return categorySum;
  }

  public String getCode() {
    return code;
  }

  public void setCategorySum(Integer categorySum) {
    this.categorySum = categorySum;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
