package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * scm分类与cnki分类对应表
 * 
 */
@Entity
@Table(name = "CATEGORY_MAP_SCM_CNKI")
public class CategoryMapScmCnki implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -8750545502559380921L;

  @Id
  @Column(name = "ID")
  private Long Id;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  @Column(name = "SCM_CATEGORY_ZH")
  private String scmCategoryZh;

  @Column(name = "CNKI_CATEGORY_ID")
  private Long cnkiCategoryId;

  @Column(name = "CNKI_CATEGORY_ZH")
  private String cnkiCategoryZh;

  public CategoryMapScmCnki() {
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

  public Long getCnkiCategoryId() {
    return cnkiCategoryId;
  }

  public void setCnkiCategoryId(Long cnkiCategoryId) {
    this.cnkiCategoryId = cnkiCategoryId;
  }

  public String getCnkiCategoryZh() {
    return cnkiCategoryZh;
  }

  public void setCnkiCategoryZh(String cnkiCategoryZh) {
    this.cnkiCategoryZh = cnkiCategoryZh;
  }

}
