package com.smate.center.batch.model.pdwh.pub;

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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊处理变化表，即期刊，折分，合并，更名。.
 */
@Entity
@Table(name = "BASE_JOURNAL_HISTORY")
public class BaseJournalHistory implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 8987032203106102358L;

  private Long jouDbId;

  private BaseJournal journal;
  // 1合并，2拆分， 3更名
  private Long action;
  // 被修改后，合并后，折分前JNL_ID
  private Long likeJouId;
  // 修改时间,日期类型，到月份
  private String beginyear;

  @Id
  @Column(name = "JOU_HISTORY_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_HISTORY", allocationSize = 1)
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

  @Column(name = "ACTION")
  public Long getAction() {
    return action;
  }

  public void setAction(Long action) {
    this.action = action;
  }

  @Column(name = "LIKE_JOU_ID")
  public Long getLikeJouId() {
    return likeJouId;
  }

  public void setLikeJouId(Long likeJouId) {
    this.likeJouId = likeJouId;
  }

  @Column(name = "BEGINYEAR")
  public String getBeginyear() {
    return beginyear;
  }

  public void setBeginyear(String beginyear) {
    this.beginyear = beginyear;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
