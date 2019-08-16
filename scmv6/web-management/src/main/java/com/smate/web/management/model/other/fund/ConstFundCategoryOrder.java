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
 * 基金推荐类别排序表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY_ORDER")
public class ConstFundCategoryOrder implements Serializable {

  private static final long serialVersionUID = 8254198524060753954L;
  private Long id;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  private Integer orderNum;
  private Integer psnInsType;
  private Integer starNum;

  public ConstFundCategoryOrder() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_CAT_ORDER", allocationSize = 1)
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

  @Column(name = "ORDER_NUM")
  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
  }

  @Column(name = "PSN_INS_TYPE")
  public Integer getPsnInsType() {
    return psnInsType;
  }

  public void setPsnInsType(Integer psnInsType) {
    this.psnInsType = psnInsType;
  }

  @Column(name = "STAR_NUM")
  public Integer getStarNum() {
    return starNum;
  }

  public void setStarNum(Integer starNum) {
    this.starNum = starNum;
  }

}
