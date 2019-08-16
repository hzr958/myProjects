package com.smate.sie.core.base.utils.model.psn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 日志操作实体类
 */
@Entity
@Table(name = "PSN_INS_LOG")
public class SieInsPsnLog {
  /**
   * 
   */
  private static final long serialVersionUID = 4667967566491493844L;

  private Long id;
  // /操作人
  private Long opPsn;
  // 操作单位
  private Long opIns;
  // 被操作人
  private Long targetPsn;
  // 操作类型
  private String action;
  // 操作详情
  private String detail;
  // 操作时间
  private Date at;

  public SieInsPsnLog() {
    super();
  }

  public SieInsPsnLog(Long opPsn, Long opIns, Long targetPsn, String action, String detail) {
    super();
    this.opPsn = opPsn;
    this.opIns = opIns;
    this.targetPsn = targetPsn;
    this.action = action;
    this.detail = detail;
    this.at = new Date();
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_INS_LOG", allocationSize = 1)
  public Long getId() {
    return id;
  }

  @Column(name = "OP_PSN")
  public Long getOpPsn() {
    return opPsn;
  }

  @Column(name = "OP_INS")
  public Long getOpIns() {
    return opIns;
  }

  @Column(name = "TARGET_PSN")
  public Long getTargetPsn() {
    return targetPsn;
  }

  @Column(name = "ACTION")
  public String getAction() {
    return action;
  }

  @Column(name = "DETAIL")
  public String getDetail() {
    return detail;
  }

  @Column(name = "AT")
  public Date getAt() {
    return at;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setOpPsn(Long opPsn) {
    this.opPsn = opPsn;
  }

  public void setOpIns(Long opIns) {
    this.opIns = opIns;
  }

  public void setTargetPsn(Long targetPsn) {
    this.targetPsn = targetPsn;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public void setAt(Date at) {
    this.at = at;
  }
}
