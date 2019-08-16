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
 * 单位基金检索结果临时表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "INS_FUND_SEARCH")
public class InsFundSearch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2930014859677677851L;
  private Long id;
  // 单位Id
  private Long insId;
  // 人员id
  private Long psnId;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  // 机构id，关联const_fund_agency表主键
  private Long agencyId;
  // 修改时间(单位进行录入新增、修改、导入新增时都要变化这个时间) ROL-1806
  private Date editDate;

  public InsFundSearch() {
    super();
  }

  public InsFundSearch(Long categoryId, Long agencyId) {
    super();
    this.categoryId = categoryId;
    this.agencyId = agencyId;
  }

  public InsFundSearch(Long categoryId, Long agencyId, Date editDate) {
    super();
    this.categoryId = categoryId;
    this.agencyId = agencyId;
    this.editDate = editDate;
  }

  public InsFundSearch(Long agencyId, Date editDate) {
    super();
    this.agencyId = agencyId;
    this.editDate = editDate;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_FUND_SEARCH", allocationSize = 1)
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

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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
