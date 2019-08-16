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
 * @author jszhou
 *
 */
@Entity
@Table(name = "IMPORT_PDWH_PUB_ERROR")
public class ImportPdwhPubError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2111661831382630176L;
  // 错误信息表主键ID
  private Long importErrorId;
  // 基准库成果ID
  private Long pdwhPubId;
  // 时间
  private Date errDate;
  // 出错信息
  private String errMsg;

  public ImportPdwhPubError() {
    super();
  }

  public ImportPdwhPubError(Long pdwhPubId, Date errDate, String errMsg) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.errDate = errDate;
    this.errMsg = errMsg;
  }

  public ImportPdwhPubError(Long importErrorId, Long pdwhPubId, Date errDate, String errMsg) {
    super();
    this.importErrorId = importErrorId;
    this.pdwhPubId = pdwhPubId;
    this.errDate = errDate;
    this.errMsg = errMsg;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IMPORT_PDWH_PUB_ERROR", allocationSize = 1)
  public Long getImportErrorId() {
    return importErrorId;
  }

  public void setImportErrorId(Long importErrorId) {
    this.importErrorId = importErrorId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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
