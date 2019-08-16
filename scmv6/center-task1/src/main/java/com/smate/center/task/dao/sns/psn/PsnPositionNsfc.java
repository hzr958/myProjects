package com.smate.center.task.dao.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员在NSFC的职称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_POSTION_NSFC")
public class PsnPositionNsfc implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7992386103172380802L;
  private Long psnId;
  // 职称CODE
  private Long code;
  // 职称英文名
  private String enTitle;
  // 职称中文名
  private String zhIitle;
  // 职称等级
  private Integer grade = 4;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CODE")
  public Long getCode() {
    return code;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  @Column(name = "ZH_TITLE")
  public String getZhIitle() {
    return zhIitle;
  }

  @Column(name = "GRADE")
  public Integer getGrade() {
    return grade;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setZhIitle(String zhIitle) {
    this.zhIitle = zhIitle;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

}
