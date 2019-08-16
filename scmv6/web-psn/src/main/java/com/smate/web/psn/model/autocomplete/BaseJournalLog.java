package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊导入日志表.
 */
@Entity
@Table(name = "BASE_JOURNAL_LOG")
public class BaseJournalLog implements Serializable {

  private static final long serialVersionUID = -1345425699669392434L;

  private Long jnlLogId;

  private Integer impCount;

  private Integer batchCount;

  private Integer manualCount;

  private String message;

  private Date createDate;

  private Integer updateCount;

  private String imp_no;

  private Long isShow;

  private Long batchTmpId;

  private Long jnlMatchBaseJnlId;

  public BaseJournalLog() {
    super();
    this.createDate = new Date();
    this.isShow = 0L;
  }

  @Id
  @Column(name = "JNL_LOG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOB_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJnlLogId() {
    return jnlLogId;
  }

  public void setJnlLogId(Long jnlLogId) {
    this.jnlLogId = jnlLogId;
  }

  @Column(name = "IMPORT_COUNT")
  public Integer getImpCount() {
    return impCount;
  }

  public void setImpCount(Integer impCount) {
    this.impCount = impCount;
  }

  @Column(name = "BATCH_COUNT")
  public Integer getBatchCount() {
    return batchCount;
  }

  public void setBatchCount(Integer batchCount) {
    this.batchCount = batchCount;
  }

  @Column(name = "MANUAL_COUNT")
  public Integer getManualCount() {
    return manualCount;
  }

  public void setManualCount(Integer manualCount) {
    this.manualCount = manualCount;
  }

  @Column(name = "MESSAGE")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "UPDATE_COUNT")
  public Integer getUpdateCount() {
    return updateCount;
  }

  public void setUpdateCount(Integer updateCount) {
    this.updateCount = updateCount;
  }

  @Column(name = "IMP_NO")
  public String getImp_no() {
    return imp_no;
  }

  public void setImp_no(String impNo) {
    imp_no = impNo;
  }

  @Column(name = "IS_SHOW")
  public Long getIsShow() {
    return isShow;
  }

  public void setIsShow(Long isShow) {
    this.isShow = isShow;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public Long getJnlMatchBaseJnlId() {
    return jnlMatchBaseJnlId;
  }

  public void setJnlMatchBaseJnlId(Long jnlMatchBaseJnlId) {
    this.jnlMatchBaseJnlId = jnlMatchBaseJnlId;
  }

  @Transient
  public Long getBatchTmpId() {
    return batchTmpId;
  }

  public void setBatchTmpId(Long batchTmpId) {
    this.batchTmpId = batchTmpId;
  }

}
