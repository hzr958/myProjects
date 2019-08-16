package com.smate.center.job.web.support.jqgrid;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.web.support.jqgrid.GroupOp.GroupOpDeserializer;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * JqGrid搜索过滤条件 页面模型类
 *
 * @author houchuanjie
 * @date 2018年3月1日 上午9:49:21
 */
public class SearchFilter {

  /**
   * 搜索条件过滤组的操作类型
   */
  @JsonDeserialize(using = GroupOpDeserializer.class)
  private GroupOp groupOp;
  /**
   * 搜索条件组
   */
  private List<SearchCriterion> rules;

  public GroupOp getGroupOp() {
    return groupOp;
  }

  public void setGroupOp(GroupOp groupOp) {
    this.groupOp = groupOp;
  }

  /**
   * @return rules
   */
  public List<SearchCriterion> getRules() {
    return rules;
  }

  /**
   * @param rules 要设置的 rules
   */
  public void setRules(List<SearchCriterion> rules) {
    this.rules = rules;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("groupOp", groupOp)
        .append("rules", rules)
        .toString();
  }
}
