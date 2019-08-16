package com.smate.web.v8pub.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 奖项信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwardsInfoBean extends PubTypeInfoBean {

  private String category = new String(); // 奖项类别

  private String grade = new String(); // 奖项等级

  private String issuingAuthority = new String(); // 颁发机构名称

  private Long issueInsId; // 颁发机构id

  private String awardDate = new String(); // 授奖日期

  private String certificateNo = new String(); // 证书编号

  /**
   * 奖项类别
   * 
   * @return
   */
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * 奖项等级
   * 
   * @return
   */
  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  /**
   * 颁发机构
   * 
   * @return
   */
  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  /**
   * 颁发机构单位id
   * 
   * @return
   */
  public Long getIssueInsId() {
    return issueInsId;
  }

  public void setIssueInsId(Long issueInsId) {
    this.issueInsId = issueInsId;
  }

  /**
   * 奖励日期
   * 
   * @return
   */
  public String getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(String awardDate) {
    this.awardDate = awardDate;
  }

  /**
   * 证书编号
   * 
   * @return
   */
  public String getCertificateNo() {
    return certificateNo;
  }

  public void setCertificateNo(String certificateNo) {
    this.certificateNo = certificateNo;
  }

  @Override
  public String toString() {
    return "AwardsInfoBean{" + "category='" + category + '\'' + ", grade='" + grade + '\'' + ", issuingAuthority='"
        + issuingAuthority + '\'' + ", issueInsId='" + issueInsId + '\'' + ", awardDate='" + awardDate + '\''
        + ", certificateNo='" + certificateNo + '\'' + '}';
  }
}
