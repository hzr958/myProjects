package com.smate.web.management.model.other.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金过滤条件.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CONDITION")
public class ConstFundCondition implements Serializable {
  private static final long serialVersionUID = 6368194804863961035L;
  private Integer id;
  private String condition;
  private String param;
  private String description;
  private int expType;// 类型：1：spring el表达式；2：hql；3：sql

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "CONDITION")
  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  @Column(name = "PARAM")
  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "EXP_TYPE")
  public int getExpType() {
    return expType;
  }

  public void setExpType(int expType) {
    this.expType = expType;
  }

}
