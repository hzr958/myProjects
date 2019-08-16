package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scm分类与nsfc分类对应表
 * 
 */
@Entity
@Table(name = "CATEGORY_MAP_SCM_NSFC")
public class CategoryMapScmNsfc implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7594498670586788368L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  @Column(name = "SCM_CATEGORY_ZH")
  private String scmCategoryZh;

  @Column(name = "NSFC_CATEGORY_ID")
  private String nsfcCategoryId;

  @Column(name = "NSFC_CATEGORY_ZH")
  private String nsfcCatogoryZh;

  public CategoryMapScmNsfc() {
    super();
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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


  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }


  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

  public String getNsfcCatogoryZh() {
    return nsfcCatogoryZh;
  }

  public void setNsfcCatogoryZh(String nsfcCatogoryZh) {
    this.nsfcCatogoryZh = nsfcCatogoryZh;
  }

}
