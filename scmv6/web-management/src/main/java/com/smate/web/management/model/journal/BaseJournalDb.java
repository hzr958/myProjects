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
  private Long jnlId;
  private Long dbId;
  private String year;

  public BaseJournalDb() {
    super();
  }

  public BaseJournalDb(Long jnlId, Long dbId, String year) {
    super();
    this.jnlId = jnlId;
    this.dbId = dbId;
    this.year = year;
  }

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

  @Column(name = "YEAR")
  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

}
