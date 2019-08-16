package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 学位论文
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThesisInfoDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = 3954736396130470997L;

  private String issuingAuthority; // 颁发单位

  private Integer degreeCode;

  private String degreeName; // 学位

  private String department; // 部门

  private String defenseDate; // 答辩日期

  /**
   * 颁发机构，签发机关
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
   * 部门
   * 
   * @return
   */
  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * 答辩日期
   * 
   * @return
   */
  public String getDefenseDate() {
    return defenseDate;
  }

  public void setDefenseDate(String defenseDate) {
    this.defenseDate = defenseDate;
  }

  public Integer getDegreeCode() {
    return degreeCode;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeCode(Integer degreeCode) {
    this.degreeCode = degreeCode;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  @Override
  public String toString() {
    return "ThesisInfoBean [issuingAuthority=" + issuingAuthority + ", degreeCode=" + degreeCode + ", degreeName="
        + degreeName + ", department=" + department + ", defenseDate=" + defenseDate + "]";
  }

}
