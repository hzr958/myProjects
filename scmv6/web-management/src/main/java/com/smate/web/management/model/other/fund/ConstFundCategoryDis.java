package com.smate.web.management.model.other.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金机构类别-领域.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY_DISCIPLINE")
public class ConstFundCategoryDis implements Serializable {

  private static final long serialVersionUID = -6373159627351719300L;
  private Long id;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  // 学科领域ID，参照const_discipline表中的ID
  private Long disId;
  // 父一级学科领域ID
  private Long superDisId;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_CAT_DIS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FUND_CATEGORY_ID")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  @Column(name = "SUPER_DIS_ID")
  public Long getSuperDisId() {
    return superDisId;
  }

  public void setSuperDisId(Long superDisId) {
    this.superDisId = superDisId;
  }

}
