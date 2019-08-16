package com.smate.web.fund.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 基金领域类
 * 
 * @author zhongjinghong
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY_REGION")
public class ConstFundCategoryRegion implements Serializable {
  private static final long serialVersionUID = 26171150448971399L;
  private Long id;
  private Long fundCategoryId;
  private Long regId;
  private String viewName;

  public ConstFundCategoryRegion() {
    super();
    // TODO Auto-generated constructor stub
  }

  public ConstFundCategoryRegion(Long id, Long fundCategoryId, Long regId, String viewName) {
    super();
    this.id = id;
    this.fundCategoryId = fundCategoryId;
    this.regId = regId;
    this.viewName = viewName;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_CAT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FUND_CATEGORY_ID")
  public Long getFundCategoryId() {
    return fundCategoryId;
  }

  public void setFundCategoryId(Long fundCategoryId) {
    this.fundCategoryId = fundCategoryId;
  }

  @Column(name = "REG_ID")
  public Long getRegId() {
    return regId;
  }

  public void setRegId(Long regId) {
    this.regId = regId;
  }

  @Column(name = "VIEW_NAME")
  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}

