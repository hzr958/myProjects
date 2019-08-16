package com.smate.center.oauth.model.interconnection;

import java.io.Serializable;

/**
 * 快速登录显示的用户信息
 * 
 * @author zll
 */

public class PsnInfo implements Serializable {


  private static final long serialVersionUID = 1L;

  private Long psnId;// 人员Id
  private String des3PsnId;// 加密的人员Id
  private String psnName;// 人员姓名
  private String firstName;// 人员姓
  private String lastName;// 人员名
  private String sex;// 性别
  private Long unitId;// 单位Id
  private String unitName;// 单位名称
  private String degree;// 职称
  private String avatorUrl;// 头像地址
  private Long insId; // 机构Id
  private String insName; // 机构名称
  private String email; // 邮箱
  private Integer PubSum; // 成果总数
  private Integer patentSum; // 专利总数
  private String department_zh; // 中文部门名称
  private String department_en; // 英文部门名称
  private String position_zh; // 中文职称
  private String position_en; // 英文职称

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getAvatorUrl() {
    return avatorUrl;
  }

  public void setAvatorUrl(String avatorUrl) {
    this.avatorUrl = avatorUrl;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getPubSum() {
    return PubSum;
  }

  public void setPubSum(Integer pubSum) {
    PubSum = pubSum;
  }

  public Integer getPatentSum() {
    return patentSum;
  }

  public void setPatentSum(Integer patentSum) {
    this.patentSum = patentSum;
  }

  public String getDepartment_zh() {
    return department_zh;
  }

  public void setDepartment_zh(String department_zh) {
    this.department_zh = department_zh;
  }

  public String getDepartment_en() {
    return department_en;
  }

  public void setDepartment_en(String department_en) {
    this.department_en = department_en;
  }

  public String getPosition_zh() {
    return position_zh;
  }

  public void setPosition_zh(String position_zh) {
    this.position_zh = position_zh;
  }

  public String getPosition_en() {
    return position_en;
  }

  public void setPosition_en(String position_en) {
    this.position_en = position_en;
  }


}
