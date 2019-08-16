package com.smate.center.open.model.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金机构类别-地区.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY_REGION")
public class ConstFundCategoryRegion implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2783213075132193376L;
  private Long id;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  // 国家地区ID，参照const_region表中的ID
  private Long regId;
  // 显示名称
  private String viewName;

  public ConstFundCategoryRegion() {
    super();
  }

  public ConstFundCategoryRegion(Long regId, String viewName) {
    super();
    this.regId = regId;
    this.viewName = viewName;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_REGION", allocationSize = 1)
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

  @Column(name = "REG_ID")
  public Long getRegId() {
    return regId;
  }

  public void setRegId(Long regId) {
    this.regId = regId;
  }

  @Column(name = "VIEW_NAME")
  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

}
