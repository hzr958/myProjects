package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊分类关系表.
 */
@Entity
@Table(name = "BASE_JOURNAL_CATEGORY")
public class BaseJournalCategory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 754522215662313552L;
  // 主键
  private Long id;
  private BaseJournal journal;
  private Long categoryId;
  private Long dbId;
  private String dbCode;
  private Long disId;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "JNL_ID", insertable = true, updatable = true)
  public BaseJournal getJournal() {
    return journal;
  }

  public void setJournal(BaseJournal journal) {
    this.journal = journal;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
