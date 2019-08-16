package com.smate.center.task.model.fund.sns;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "V_FUND_RECOMMEND_SENIOR")
public class FundRecommendSeniority {
  private static final long serialVersionUID = -7345950806158199813L;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_RECOMMEND_SENIOR", allocationSize = 1)
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "SENIORITY_CODE")
  private Integer code;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

}
