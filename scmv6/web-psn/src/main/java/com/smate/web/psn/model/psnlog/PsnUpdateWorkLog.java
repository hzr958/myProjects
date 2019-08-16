package com.smate.web.psn.model.psnlog;

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
 * 更新工作经历后,保存日志，发邮件通知好友 zk add
 */
@Entity
@Table(name = "PSN_UPDATE_WORK_LOG")
public class PsnUpdateWorkLog implements Serializable {

  private static final long serialVersionUID = -7327602781637654972L;
  private Long id;
  private Long psnId;
  private Long workId;
  private Date createDate;
  private Integer status;

  @Id
  @Column(name = "")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_UPDATE_WORK_LOG", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "work_id")
  public Long getWorkId() {
    return workId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Column(name = "create_date")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
