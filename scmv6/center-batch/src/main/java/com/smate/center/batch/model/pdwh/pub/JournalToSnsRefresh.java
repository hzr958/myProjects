package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 期刊冗余到sns刷新表
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "task_sycn_sns_journal_ids")
public class JournalToSnsRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7577401161920077837L;
  private Long id;
  private Long jid; //
  private Integer jidStatus;
  private Long bjid; // 基础期刊id
  private Integer bjidStatus; // 基础集刊ID 匹配状态

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_task_sycn_sns_journal_ids", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "jid")
  public Long getJid() {
    return jid;
  }

  @Column(name = "jid_status")
  public Integer getJidStatus() {
    return jidStatus;
  }

  @Column(name = "match_base_jnl_id")
  public Long getBjid() {
    return bjid;
  }

  @Column(name = "match_base_status")
  public Integer getBjidStatus() {
    return bjidStatus;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public void setJidStatus(Integer jidStatus) {
    this.jidStatus = jidStatus;
  }

  public void setBjid(Long bjid) {
    this.bjid = bjid;
  }

  public void setBjidStatus(Integer bjidStatus) {
    this.bjidStatus = bjidStatus;
  }

}

