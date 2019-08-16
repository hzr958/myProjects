package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cwli期刊分类关系表.
 */
@Entity
@Table(name = "BASE_JOURNAL_CATEGORY")
public class BaseJournalCategory implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5568007817266691263L;

  // 主键
  private Long id;
  private Long jnlId;
  private Long categoryId;
  private Long dbId;
  private Long disId;

  public BaseJournalCategory() {
    super();
  }

  public BaseJournalCategory(Long jnlId, Long categoryId, Long dbId) {
    super();
    this.jnlId = jnlId;
    this.categoryId = categoryId;
    this.dbId = dbId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOURNAL_CATEGORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "CATEGORY_ID")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

}
