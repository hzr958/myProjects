package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊被收录表.
 */
@Entity
@Table(name = "BASE_JOURNAL_DB")
public class BaseJournalDb implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5047957224985281380L;
  private Long jouDbId;
  private BaseJournal journal;
  private Long dbId;
  private String year;
  private String dbCode;

  @Id
  @Column(name = "JOU_DB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_DB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJouDbId() {
    return jouDbId;
  }

  public void setJouDbId(Long jouDbId) {
    this.jouDbId = jouDbId;
  }

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "JNL_ID", insertable = true, updatable = true)
  public BaseJournal getJournal() {
    return journal;
  }

  public void setJournal(BaseJournal journal) {
    this.journal = journal;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "YEAR")
  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
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
