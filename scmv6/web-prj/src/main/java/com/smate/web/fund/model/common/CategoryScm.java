package com.smate.web.fund.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY_SCM")
public class CategoryScm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5402527447215113908L;

  @Id
  @Column(name = "CATEGRY_ID")
  private Long categoryId;
  @Column(name = "CATEGORY_ZH")
  private String categoryZh;
  @Column(name = "CATEGORY_EN")
  private String categoryEn;
  @Column(name = "PARENT_CATEGORY_ID")
  private Long parentCategroyId;

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryZh() {
    return categoryZh;
  }

  public void setCategoryZh(String categoryZh) {
    this.categoryZh = categoryZh;
  }

  public String getCategoryEn() {
    return categoryEn;
  }

  public void setCategoryEn(String categoryEn) {
    this.categoryEn = categoryEn;
  }

  public Long getParentCategroyId() {
    return parentCategroyId;
  }

  public void setParentCategroyId(Long parentCategroyId) {
    this.parentCategroyId = parentCategroyId;
  }

}
