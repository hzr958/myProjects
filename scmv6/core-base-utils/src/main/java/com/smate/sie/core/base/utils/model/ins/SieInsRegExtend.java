package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位注册拓展表.
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INS_REG_EXTEND")
public class SieInsRegExtend implements Serializable {

  private static final long serialVersionUID = 5960074245120561001L;

  // 注册单位ID
  private Long insregId;
  // 单位邮箱服务器后缀列表
  private String checkEmails;
  // 邮编
  private String postcode;
  // 传真
  private String fox;
  // 市一级的名称
  private String regionName;
  // 统一社会信用代码
  private String uniformId1;
  // 组织机构代码
  private String uniformId2;

  @Id
  @Column(name = "INSREG_ID")
  public Long getInsregId() {
    return insregId;
  }

  @Column(name = "CHECK_EMAILS")
  public String getCheckEmails() {
    return checkEmails;
  }

  public void setCheckEmails(String checkEmails) {
    this.checkEmails = checkEmails;
  }

  @Column(name = "POST_CODE")
  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Column(name = "FOX")
  public String getFox() {
    return fox;
  }

  public void setFox(String fox) {
    this.fox = fox;
  }

  @Column(name = "REGION_NAME")
  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  @Column(name = "UNIFORM_ID1")
  public String getUniformId1() {
    return uniformId1;
  }

  public void setUniformId1(String uniformId1) {
    this.uniformId1 = uniformId1;
  }

  @Column(name = "UNIFORM_ID2")
  public String getUniformId2() {
    return uniformId2;
  }

  public void setUniformId2(String uniformId2) {
    this.uniformId2 = uniformId2;
  }

  public void setInsregId(Long insregId) {
    this.insregId = insregId;
  }


}
