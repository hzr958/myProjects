package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 期刊学科分类与学科代码对应表
 * 
 * @author zyx
 *
 */
@Entity
@Table(name = "CONST_CATEGORY_DISCIPLINE")
public class ConstCategoryDiscipline implements Serializable {

  private static final long serialVersionUID = -7588308162779654380L;

  private Long id;
  private Long categoryId;
  private Long disId;
  private Long superDiscid;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "CATEGORY_ID")
  public Long getCategoryId() {
    return categoryId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  @Column(name = "SUPER_DISCID")
  public Long getSuperDiscid() {
    return superDiscid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setSuperDiscid(Long superDiscid) {
    this.superDiscid = superDiscid;
  }
}
