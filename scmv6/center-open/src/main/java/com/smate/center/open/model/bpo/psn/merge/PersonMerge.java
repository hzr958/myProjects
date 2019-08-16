package com.smate.center.open.model.bpo.psn.merge;

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
 * 人员合并model.
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "PERSON_MERGE_TASK")
public class PersonMerge implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3452390516931959339L;
  private Long id;
  // 合并人主键
  private Long savePsnId;
  // 被合并人主键
  private Long delPsnId;
  // 错误信息
  private String errMsg;
  // 合并日期
  private Date createDate;
  // 合并状态 0=需要合并
  private Long status;
  // 动态合并状态
  private Long statusExt;

  public PersonMerge() {
    super();
  }

  public PersonMerge(Long savePsnId, Long delPsnId, String errMsg, Date createDate, Long status, Long statusExt) {
    super();
    this.savePsnId = savePsnId;
    this.delPsnId = delPsnId;
    this.errMsg = errMsg;
    this.createDate = createDate;
    this.status = status;
    this.statusExt = statusExt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON_MERGE_TASK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "SAVE_PSN_ID")
  public Long getSavePsnId() {
    return savePsnId;
  }

  @Column(name = "DEL_PSN_ID")
  public Long getDelPsnId() {
    return delPsnId;
  }

  @Column(name = "ERR_MSG")
  public String getErrMsg() {
    return errMsg;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setSavePsnId(Long savePsnId) {
    this.savePsnId = savePsnId;
  }

  public void setDelPsnId(Long delPsnId) {
    this.delPsnId = delPsnId;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Column(name = "STATUS_EXT")
  public Long getStatusExt() {
    return statusExt;
  }

  public void setStatusExt(Long statusExt) {
    this.statusExt = statusExt;
  }

}
