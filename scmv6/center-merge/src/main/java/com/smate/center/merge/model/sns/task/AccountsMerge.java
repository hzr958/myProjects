package com.smate.center.merge.model.sns.task;

import javax.persistence.*;
import java.util.Date;

/**
 * 帐号合并实体.
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_ACCOUNTS_MERGE_TASK")
public class AccountsMerge {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_ACCOUNTS_MERGE_TASK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  // 合并人主键
  @Column(name = "SAVE_PSN_ID")
  private Long savePsnId;
  // 被合并人主键
  @Column(name = "DEL_PSN_ID")
  private Long delPsnId;
  // 错误信息
  @Column(name = "ERR_MSG")
  private String errMsg;
  // 合并日期
  @Column(name = "CREATE_DATE")
  private Date createDate;
  // 合并状态 0=需要合并
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "STATUS_EXT")
  private Long statusExt;

  public AccountsMerge() {
    super();
  }

  /**
   * 函数. 2018年9月13日
   */
  public AccountsMerge(Long id, Long savePsnId, Long delPsnId, String errMsg, Date createDate, Long status,
      Long statusExt) {
    super();
    this.id = id;
    this.savePsnId = savePsnId;
    this.delPsnId = delPsnId;
    this.errMsg = errMsg;
    this.createDate = createDate;
    this.status = status;
    this.statusExt = statusExt;
  }

  public Long getStatusExt() {
    return statusExt;
  }

  public void setStatusExt(Long statusExt) {
    this.statusExt = statusExt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSavePsnId() {
    return savePsnId;
  }

  public void setSavePsnId(Long savePsnId) {
    this.savePsnId = savePsnId;
  }

  public Long getDelPsnId() {
    return delPsnId;
  }

  public void setDelPsnId(Long delPsnId) {
    this.delPsnId = delPsnId;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }
}
