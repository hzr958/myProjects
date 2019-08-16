package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊常数分类主表.
 */
@Entity
@Table(name = "BASE_CONST_CATEGORY")
public class BaseConstCategory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6443829657207674634L;

  // 主键
  private Long categoryId;

  private String categoryXx;

  private String categoryEn;

  private Long dbId;

  private Long parentId;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_CONST_CATEGORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "CATEGORY_XX")
  public String getCategoryXx() {
    return categoryXx;
  }

  public void setCategoryXx(String categoryXx) {
    this.categoryXx = categoryXx;
  }

  @Column(name = "CATEGORY_EN")
  public String getCategoryEn() {
    return categoryEn;
  }

  public void setCategoryEn(String categoryEn) {
    this.categoryEn = categoryEn;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PARENT_ID")
  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
