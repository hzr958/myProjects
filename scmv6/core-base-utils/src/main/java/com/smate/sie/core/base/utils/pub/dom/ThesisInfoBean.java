package com.smate.sie.core.base.utils.pub.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 学位论文
 * 
 * @author sjzhou
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThesisInfoBean extends PubTypeInfoBean {

  private String issuingAuthority = new String(); // 颁证单位

  private String degreeCode = new String();

  private String degreeName = new String(); // 学位

  private String department = new String(); // 部门

  private String defenseDate = new String(); // 答辩日期

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

  public String getDegreeCode() {
    return degreeCode;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeCode(String degreeCode) {
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
