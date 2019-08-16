package com.smate.web.v8pub.po.journal;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

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
  private BaseJournalPO journal;
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
  public BaseJournalPO getJournal() {
    return journal;
  }

  public void setJournal(BaseJournalPO journal) {
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
