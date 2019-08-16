package com.smate.web.v8pub.po.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CATEGORY_MAP_BASE")
public class CategoryMapBase implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6291534942118336726L;

  private Integer categryId; // Id
  private String categoryZh; // 科技领域中文名
  private String categoryEn; // 科技领域英文名
  private String igiZh;
  private String igiEn;
  private String moe; //
  private String moeSub; //
  private String nsfcCategory; // nsfc科研分类
  private String wosCategory; // wos科研分类
  private Integer superCategoryId; // 父级学科ID,一级学科该值为0

  public CategoryMapBase(Integer categryId, String categoryZh, String categoryEn, String igiZh, String igiEn,
      String moe, String moeSub, String nsfcCategory, String wosCategory, Integer superCategoryId) {
    super();
    this.categryId = categryId;
    this.categoryZh = categoryZh;
    this.categoryEn = categoryEn;
    this.igiZh = igiZh;
    this.igiEn = igiEn;
    this.moe = moe;
    this.moeSub = moeSub;
    this.nsfcCategory = nsfcCategory;
    this.wosCategory = wosCategory;
    this.superCategoryId = superCategoryId;
  }

  public CategoryMapBase(Integer categryId, String categoryZh, String categoryEn, Integer superCategoryId) {
    this.categryId = categryId;
    this.categoryZh = categoryZh;
    this.categoryEn = categoryEn;
    this.superCategoryId = superCategoryId;
  }

  public CategoryMapBase() {
    super();
  }

  @Id
  @Column(name = "CATEGRY_ID")
  public Integer getCategryId() {
    return categryId;
  }

  public void setCategryId(Integer categryId) {
    this.categryId = categryId;
  }

  @Column(name = "CATEGORY_ZH")
  public String getCategoryZh() {
    return categoryZh;
  }

  public void setCategoryZh(String categoryZh) {
    this.categoryZh = categoryZh;
  }

  @Column(name = "CATEGORY_EN")
  public String getCategoryEn() {
    return categoryEn;
  }

  public void setCategoryEn(String categoryEn) {
    this.categoryEn = categoryEn;
  }

  @Column(name = "IGI_ZH")
  public String getIgiZh() {
    return igiZh;
  }

  public void setIgiZh(String igiZh) {
    this.igiZh = igiZh;
  }

  @Column(name = "IGI_EN")
  public String getIgiEn() {
    return igiEn;
  }

  public void setIgiEn(String igiEn) {
    this.igiEn = igiEn;
  }

  @Column(name = "MOE")
  public String getMoe() {
    return moe;
  }

  public void setMoe(String moe) {
    this.moe = moe;
  }

  @Column(name = "MOE_SUB")
  public String getMoeSub() {
    return moeSub;
  }

  public void setMoeSub(String moeSub) {
    this.moeSub = moeSub;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategory() {
    return nsfcCategory;
  }

  public void setNsfcCategory(String nsfcCategory) {
    this.nsfcCategory = nsfcCategory;
  }

  @Column(name = "WOS_CATEGORY")
  public String getWosCategory() {
    return wosCategory;
  }

  public void setWosCategory(String wosCategory) {
    this.wosCategory = wosCategory;
  }

  @Column(name = "SUPER_CATEGORY_ID")
  public Integer getSuperCategoryId() {
    return superCategoryId;
  }

  public void setSuperCategoryId(Integer superCategoryId) {
    this.superCategoryId = superCategoryId;
  }

}
