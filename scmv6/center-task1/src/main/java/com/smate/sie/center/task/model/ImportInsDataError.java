package com.smate.sie.center.task.model;

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
 * 错误日志表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "IMPORT_INS_DATA_ERROR")
public class ImportInsDataError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2111661831382630176L;
  // 主键
  private Long id;
  // 人员主键
  private Long psnId;
  // 时间
  private Date errDate;
  // 出错信息
  private String errMsg;


  public ImportInsDataError() {
    super();
  }

  public ImportInsDataError(Long id, Long psnId, Date errDate, String errMsg) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.errDate = errDate;
    this.errMsg = errMsg;
  }



  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IMPORT_INS_DATA_ERROR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "ERROR_DATE")
  public Date getErrDate() {
    return errDate;
  }

  public void setErrDate(Date errDate) {
    this.errDate = errDate;
  }

  @Column(name = "ERROR_MSG")
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }



}
