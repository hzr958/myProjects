package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_PAY_VALIDATE_USER")
public class KpiPayValidateUser implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3512415817036191775L;
  private Long psnId;
  private Date startDate; // 开始时间
  private Date endDate;// 结束时间
  private String grade;// A B C
  // private String discount; // 折扣

  public KpiPayValidateUser() {
    super();
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "START_DATE")
  public Date getStartDate() {
    return startDate;
  }

  @Column(name = "END_DATE")
  public Date getEndDate() {
    return endDate;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }


  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Column(name = "GRADE")
  public String getGrade() {
    return grade;
  }

  // @Column(name = "DISCOUNT")
  // public String getDiscount() {
  // return discount;
  // }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  // public void setDiscount(String discount) {
  // this.discount = discount;
  // }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }


}
