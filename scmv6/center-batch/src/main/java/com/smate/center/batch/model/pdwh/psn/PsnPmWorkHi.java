package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户工作经历表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_WORK_HI")
public class PsnPmWorkHi implements Serializable {

  private static final long serialVersionUID = -5106715633740189534L;
  private Long id;// 主键.
  private Long workId;// 个人工作经历ID.
  private Long insId;// 单位ID.
  private Long psnId;// 人员ID.
  private Long fromYear;// 开始年份.
  private Long fromMonth;// 开始月份.
  private Long toYear;// 结束年份.
  private Long toMonth;// 结束月份.
  private Long isActive;// 是否至今在该单位工作(0-是；1-否).

  public PsnPmWorkHi() {
    super();
  }

  public PsnPmWorkHi(Long id, Long workId, Long insId, Long psnId, Long fromYear, Long fromMonth, Long toYear,
      Long toMonth, Long isActive) {
    super();
    this.id = id;
    this.workId = workId;
    this.insId = insId;
    this.psnId = psnId;
    this.fromYear = fromYear;
    this.fromMonth = fromMonth;
    this.toYear = toYear;
    this.toMonth = toMonth;
    this.isActive = isActive;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_WORK_HI", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "WORK_ID")
  public Long getWorkId() {
    return workId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "FROM_YEAR")
  public Long getFromYear() {
    return fromYear;
  }

  @Column(name = "FROM_MONTH")
  public Long getFromMonth() {
    return fromMonth;
  }

  @Column(name = "TO_YEAR")
  public Long getToYear() {
    return toYear;
  }

  @Column(name = "TO_MONTH")
  public Long getToMonth() {
    return toMonth;
  }

  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

}
