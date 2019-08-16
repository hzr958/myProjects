package com.iris.scm.scmweb.model.register;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 工作经历.
 * 
 * @author zt
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

  private PersonRegister person;

  private Long insId;
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
   * 加密ID.
   */
  private String des3Id;

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
   * @return the person
   */
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "PSN_ID", insertable = true, updatable = true)
  public PersonRegister getPerson() {
    return person;
  }

  /**
   * @param person the person to set
   */
  public void setPerson(PersonRegister person) {
    this.person = person;
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

  @Transient
  public String getDes3Id() {
    if (this.workId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.workId.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Column(name = "IS_PRIMARY")
  public Long getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Long isPrimary) {
    this.isPrimary = isPrimary;
  }
}
