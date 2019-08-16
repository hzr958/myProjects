package com.smate.web.v8pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 奖项信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwardsInfoDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String category; // 奖项类别

  private String grade; // 奖项等级

  private String issuingAuthority; // 颁发机构名称

  private Long issueInsId; // 颁发机构id

  private String awardDate; // 授奖日期

  private String certificateNo; // 证书编号

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

}
