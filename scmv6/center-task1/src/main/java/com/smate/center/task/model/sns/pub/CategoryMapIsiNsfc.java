package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scm分类与wos分类对应表
 * 
 */
@Entity
@Table(name = "CATEGORY_MAP_NSFC_WOS_NEW")
public class CategoryMapIsiNsfc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5431375880940253899L;

  @Id
  @Column(name = "ID")
  private Long Id;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  @Column(name = "NSFC_CATEGORY_ID")
  private String nsfcCategoryId;

  @Column(name = "NSFC_CATEGORY_ZH")
  private String nsfcCategoryEn;

  @Column(name = "WOS_CATEGORY_EN")
  private String wosCategoryEn;

  @Column(name = "WOS_CATEGORY_ZH")
  private String wosCategoryZh;

  public CategoryMapIsiNsfc() {
    super();
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }

  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }

  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

  public String getNsfcCategoryEn() {
    return nsfcCategoryEn;
  }

  public void setNsfcCategoryEn(String nsfcCategoryEn) {
    this.nsfcCategoryEn = nsfcCategoryEn;
  }

  public String getWosCategoryEn() {
    return wosCategoryEn;
  }

  public void setWosCategoryEn(String wosCategoryEn) {
    this.wosCategoryEn = wosCategoryEn;
  }

  public String getWosCategoryZh() {
    return wosCategoryZh;
  }

  public void setWosCategoryZh(String wosCategoryZh) {
    this.wosCategoryZh = wosCategoryZh;
  }

}
