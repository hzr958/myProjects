package com.smate.web.v8pub.dom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;

/**
 * 学位论文
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThesisInfoBean extends PubTypeInfoBean implements Serializable {

  private static final long serialVersionUID = 3954736396130470997L;

  private PubThesisDegreeEnum degree = PubThesisDegreeEnum.OTHER; // 学位

  private String issuingAuthority = new String(); // 签发机关，颁发单位

  private String department = new String(); // 部门

  private String ISBN = new String(); // 国际标准图书编号

  private String defenseDate = new String(); // 答辩日期

  /**
   * 学位
   * 
   * @return
   */
  public PubThesisDegreeEnum getDegree() {
    return degree;
  }

  public void setDegree(PubThesisDegreeEnum degree) {
    this.degree = degree;
  }

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

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String iSBN) {
    ISBN = iSBN;
  }

  @Override
  public String toString() {
    return "ThesisInfoBean{" + "degree='" + degree + '\'' + ", issuingAuthority='" + issuingAuthority + '\''
        + ", department='" + department + '\'' + ", defenseDate='" + defenseDate + '\'' + '}';
  }
}
