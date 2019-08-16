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
@Table(name = "CATEGORY_MAP_NSFC_CNKI")
public class CategoryMapCnkiNsfc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3492450733075739107L;

  @Id
  @Column(name = "ID")
  private Long Id;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  @Column(name = "CNKI_CATEGORY_ID")
  private Long cnkiCategoryId;

  @Column(name = "CNKI_CATEGORY_ZH")
  private String cnkiCategoryZh;

  @Column(name = "NSFC_CATEGORY_ID")
  private String nsfcCategoryId;

  @Column(name = "NSFC_CATEGORY_ZH")
  private String nsfcCategoryZh;

  public CategoryMapCnkiNsfc() {
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

  public String getNsfcCategoryZh() {
    return nsfcCategoryZh;
  }

  public void setNsfcCategoryZh(String nsfcCategoryZh) {
    this.nsfcCategoryZh = nsfcCategoryZh;
  }

}
