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
@Table(name = "CATEGORY_MAP_SCM_WOS")
public class CategoryMapScmIsi implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5958062527476698824L;

  @Id
  @Column(name = "ID")
  private Long Id;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  @Column(name = "SCM_CATEGORY_ZH")
  private String scmCategoryZh;

  @Column(name = "SCM_CATEGORY_EN")
  private String scmCategoryEn;

  @Column(name = "WOS_CATEGORY_EN")
  private String wosCategoryEn;

  @Column(name = "WOS_CATEGORY_ZH")
  private String wosCategoryZh;

  public CategoryMapScmIsi() {
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

  public String getScmCategoryZh() {
    return scmCategoryZh;
  }

  public void setScmCategoryZh(String scmCategoryZh) {
    this.scmCategoryZh = scmCategoryZh;
  }

  public String getScmCategoryEn() {
    return scmCategoryEn;
  }

  public void setScmCategoryEn(String scmCategoryEn) {
    this.scmCategoryEn = scmCategoryEn;
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
