package com.smate.center.open.model.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 工作经历.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PSN_WORK_HISTORY")
public class WorkHistoryRegister implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1628252774690314207L;

  /**
   * 主健.
   */
  private Long workId;

  private Long insId;

  private Long psnId;
  /**
   * 机构名称.
   */
  private String insName;
  /**
   * 职务.
   */
  private String position;
  private Long posId;
  private Integer posGrades;
  /**
   * 部门.
   */
  private String department;
  /**
   * 
   */
  private Long isActive;
  /**
   * 开始年份.
   */
  private Long fromYear;
  /**
   * 开始月份.
   */
  private Long fromMonth;
  /**
   * 截至年份.
   */
  private Long toYear;
  /**
   * 截至月份.
   */
  private Long toMonth;

  /**
   * 是否为首要工作单位
   */
  private Long isPrimary;

  /**
   * 
   */
  public WorkHistoryRegister() {

  }

  /**
   * @return id
   */
  @Id
  @Column(name = "WORK_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_WORK_HISTORY", allocationSize = 1)
  public Long getWorkId() {
    return workId;
  }

  /**
   * @param id
   */
  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @return institutionName
   */
  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  /**
   * @param institutionName
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @return department
   */
  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  /**
   * @param department
   */
  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * @return isActive
   */
  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  /**
   * @param isActive
   */
  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

  /**
   * @return fromYear
   */
  @Column(name = "FROM_YEAR")
  public Long getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear
   */
  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  /**
   * @return fromMonth
   */
  @Column(name = "FROM_MONTH")
  public Long getFromMonth() {
    return fromMonth;
  }

  /**
   * @param fromMonth
   */
  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  /**
   * @return toYear
   */
  @Column(name = "TO_YEAR")
  public Long getToYear() {
    return toYear;
  }

  /**
   * @param toYear
   */
  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  /**
   * @return toMonth
   */
  @Column(name = "TO_MONTH")
  public Long getToMonth() {
    return toMonth;
  }

  /**
   * @param toMonth
   */
  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  @Column(name = "POS_GRADES")
  public Integer getPosGrades() {
    return posGrades;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  @Column(name = "IS_PRIMARY")
  public Long getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Long isPrimary) {
    this.isPrimary = isPrimary;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }
}
