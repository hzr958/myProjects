package com.smate.web.management.model.other.fund;

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
 * SIE单位基金资助机构类别.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "INS_FUND")
public class InsFund implements Serializable {

  private static final long serialVersionUID = -7812411489271348560L;
  private Long id;
  // 单位Id
  private Long insId;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  // 基金机构ID，参照const_fund_agency表中的ID
  private Long agencyId;
  // 修改时间(单位进行录入新增、修改、导入新增时都要变化这个时间) ROL-1806
  private Date editDate;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_FUND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "FUND_CATEGORY_ID")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "FUND_AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Column(name = "EDIT_DATE")
  public Date getEditDate() {
    return editDate;
  }

  public void setEditDate(Date editDate) {
    this.editDate = editDate;
  }

}
