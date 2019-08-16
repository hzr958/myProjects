package com.smate.center.batch.model.dynamic;

import java.io.Serializable;

/**
 * 基金推荐动态参数实体.
 * 
 * @author mjg
 * 
 */
public class DynRecommendFundForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8476466485863498551L;
  private Long psnId;
  private Long fundId;
  private String catDes3Id;
  private String agencyViewName;
  private String agencyViewNameEn;
  private String categoryViewName;
  private String categoryViewNameEn;

  public DynRecommendFundForm() {
    super();
  }

  public DynRecommendFundForm(Long psnId, Long fundId, String catDes3Id, String agencyViewName, String agencyViewNameEn,
      String categoryViewName, String categoryViewNameEn) {
    super();
    this.psnId = psnId;
    this.fundId = fundId;
    this.catDes3Id = catDes3Id;
    this.agencyViewName = agencyViewName;
    this.agencyViewNameEn = agencyViewNameEn;
    this.categoryViewName = categoryViewName;
    this.categoryViewNameEn = categoryViewNameEn;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getCatDes3Id() {
    return catDes3Id;
  }

  public void setCatDes3Id(String catDes3Id) {
    this.catDes3Id = catDes3Id;
  }

  public String getAgencyViewName() {
    return agencyViewName;
  }

  public void setAgencyViewName(String agencyViewName) {
    this.agencyViewName = agencyViewName;
  }

  public String getAgencyViewNameEn() {
    return agencyViewNameEn;
  }

  public void setAgencyViewNameEn(String agencyViewNameEn) {
    this.agencyViewNameEn = agencyViewNameEn;
  }

  public String getCategoryViewName() {
    return categoryViewName;
  }

  public void setCategoryViewName(String categoryViewName) {
    this.categoryViewName = categoryViewName;
  }

  public String getCategoryViewNameEn() {
    return categoryViewNameEn;
  }

  public void setCategoryViewNameEn(String categoryViewNameEn) {
    this.categoryViewNameEn = categoryViewNameEn;
  }

}
