package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员职称等级，取（NSFC+SMATE）最高.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_POSTION_GRADE")
public class PsnPositionGrade implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6522915983423905502L;
  private Long psnId;
  // 职称等级
  private Integer grade;
  // 职称CODE
  private Long posCode;
  // 职称名
  private String posName;

  public PsnPositionGrade() {
    super();
  }

  public PsnPositionGrade(Long psnId, Integer grade) {
    super();
    this.psnId = psnId;
    this.grade = grade;
  }

  public PsnPositionGrade(Long psnId, Integer grade, Long posCode, String posName) {
    super();
    this.psnId = psnId;
    this.grade = grade;
    this.posCode = posCode;
    this.posName = posName;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "GRADE")
  public Integer getGrade() {
    return grade;
  }

  @Column(name = "POS_CODE")
  public Long getPosCode() {
    return posCode;
  }

  @Column(name = "POS_NAME")
  public String getPosName() {
    return posName;
  }

  public void setPosName(String posName) {
    this.posName = posName;
  }

  public void setPosCode(Long posCode) {
    this.posCode = posCode;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

}
