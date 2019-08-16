package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 获奖信息
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwardsInfoDTO extends PubTypeInfoDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8702425261690937339L;

  private String categoryCode; // 奖项类别

  private String categoryName; // 奖项类别

  private String gradeCode; // 奖项等级

  private String gradeName; // 奖项等级

  private String issuingAuthority; // 颁发机构名称

  private String awardDate; // 授奖日期

  private String certificateNo; // 证书编号

  public String getCategoryCode() {
    return categoryCode;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getGradeCode() {
    return gradeCode;
  }

  public String getGradeName() {
    return gradeName;
  }

  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public String getAwardDate() {
    return awardDate;
  }

  public String getCertificateNo() {
    return certificateNo;
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public void setGradeCode(String gradeCode) {
    this.gradeCode = gradeCode;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  public void setAwardDate(String awardDate) {
    this.awardDate = awardDate;
  }

  public void setCertificateNo(String certificateNo) {
    this.certificateNo = certificateNo;
  }

  @Override
  public String toString() {
    return "AwardsInfoBean [categoryCode=" + categoryCode + ", categoryName=" + categoryName + ", gradeCode="
        + gradeCode + ", gradeName=" + gradeName + ", issuingAuthority=" + issuingAuthority + ", awardDate=" + awardDate
        + ", certificateNo=" + certificateNo + "]";
  }

}
