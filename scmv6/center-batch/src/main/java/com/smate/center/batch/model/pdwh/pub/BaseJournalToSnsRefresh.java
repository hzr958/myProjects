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
 * 基础期刊冗余到sns刷新表
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "task_sycn_sns_base_journal_ids")
public class BaseJournalToSnsRefresh implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1366195872848957609L;
  private Long id;
  private Long bjid; // 基础期刊id
  private Integer status; // 刷新状态

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_task_sycn_sns_base_journal", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "bjid")
  public Long getBjid() {
    return bjid;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBjid(Long bjid) {
    this.bjid = bjid;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
