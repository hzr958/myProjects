package com.smate.web.psn.model.keyword;

import java.io.Serializable;

/**
 * 科技领域信息操作类
 * 
 * @author wsn
 *
 */
public class CategoryMapBaseInfo implements Serializable, Comparable<CategoryMapBaseInfo> {

  private static final long serialVersionUID = -3540098547157470732L;
  private Integer categoryId; // Id
  private String categoryZh; // 科技领域中文名
  private String categoryEn; // 科技领域英文名
  private boolean added; // 已是人员的科技领域
  private Integer superCategoryId; // 父级学科ID,一级学科该值为0
  private String showCategory; // 页面显示的科技领域名

  public CategoryMapBaseInfo() {
    super();
  }

  public void copyCategoryMapBaseInfo(CategoryMapBase info) {
    this.categoryId = info.getCategryId();
    this.categoryZh = info.getCategoryZh();
    this.categoryEn = info.getCategoryEn();
    this.superCategoryId = info.getSuperCategoryId();
    this.added = info.getAdded();
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
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

  public boolean isAdded() {
    return added;
  }

  public void setAdded(boolean added) {
    this.added = added;
  }

  public Integer getSuperCategoryId() {
    return superCategoryId;
  }

  public void setSuperCategoryId(Integer superCategoryId) {
    this.superCategoryId = superCategoryId;
  }

  public String getShowCategory() {
    return showCategory;
  }

  public void setShowCategory(String showCategory) {
    this.showCategory = showCategory;
  }

  @Override
  public int compareTo(CategoryMapBaseInfo o) {
    return this.getCategoryId() - o.getCategoryId();
  }

}
