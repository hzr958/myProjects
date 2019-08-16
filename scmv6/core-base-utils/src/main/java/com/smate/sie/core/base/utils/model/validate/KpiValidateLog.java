package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
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
 * @author ztg
 *
 */
@Entity
@Table(name = "KPI_VALIDATE_LOG")
public class KpiValidateLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -301814609956365608L;

  /* 主键 */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_VALIDATE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  /* kpi_validate_detail表主键 */
  @Column(name = "DETAIL_ID")
  private Long detailId;

  /* 每次接口调用时间 */
  @Column(name = "CREATE_TIME")
  private Date creTime;

  /* 每次接口返回信息中的MSG字段 */
  @Column(name = "MSG")
  private String msg;

  /* 后台任务调用接口日志 */
  @Column(name = "DETAIL_LOG")
  private String detailLog;

  /* 每次接口返回信息中的MSG字段 */
  @Column(name = "MSG_ALL")
  private String msgAll;

  public KpiValidateLog() {
    super();
  }

  public KpiValidateLog(Long detailId, Date creTime, String msg, String msgAll) {
    super();
    this.detailId = detailId;
    this.creTime = creTime;
    this.msg = msg;
    this.msgAll = msgAll;
  }

  public KpiValidateLog(Long detailId, Date creTime, String msg, String msgAll, String detailLog) {
    super();
    this.detailId = detailId;
    this.creTime = creTime;
    this.msg = msg;
    this.msgAll = msgAll;
    this.detailLog = detailLog;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDetailId() {
    return detailId;
  }

  public void setDetailId(Long detailId) {
    this.detailId = detailId;
  }

  public Date getCreTime() {
    return creTime;
  }

  public void setCreTime(Date creTime) {
    this.creTime = creTime;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getMsgAll() {
    return msgAll;
  }

  public void setMsgAll(String msgAll) {
    this.msgAll = msgAll;
  }

  @Override
  public String toString() {
    return "KpiValidateLog [id=" + id + ", detailId=" + detailId + ", creTime=" + creTime + ", msg=" + msg + ", msgAll="
        + msgAll + "]";
  }
}
