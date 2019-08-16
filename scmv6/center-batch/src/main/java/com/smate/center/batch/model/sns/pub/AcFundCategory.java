package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金申请类别自动提示.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "FUND_CATEGORY")
public class AcFundCategory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1919061032445948599L;
  private Long id;
  private int agencyFlag;
  private String category;
  private String enCategory;
  private String fullCategory;
  private String enFullCategory;

  public AcFundCategory() {}

  public AcFundCategory(Long id, String category) {
    this.id = id;
    this.category = category;
  }

  @Id
  @Column(name = "TYPE_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "AGENCY_FLAG")
  public int getAgencyFlag() {
    return agencyFlag;
  }

  public void setAgencyFlag(int agencyFlag) {
    this.agencyFlag = agencyFlag;
  }

  @Column(name = "CATEGORY")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Column(name = "EN_CATEGORY")
  public String getEnCategory() {
    return enCategory;
  }

  public void setEnCategory(String enCategory) {
    this.enCategory = enCategory;
  }

  @Column(name = "FULL_CATEGORY")
  public String getFullCategory() {
    return fullCategory;
  }

  public void setFullCategory(String fullCategory) {
    this.fullCategory = fullCategory;
  }

  @Column(name = "EN_FULL_CATEGORY")
  public String getEnFullCategory() {
    return enFullCategory;
  }

  public void setEnFullCategory(String enFullCategory) {
    this.enFullCategory = enFullCategory;
  }

}
