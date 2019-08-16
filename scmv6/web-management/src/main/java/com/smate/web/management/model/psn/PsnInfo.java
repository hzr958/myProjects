package com.smate.web.management.model.psn;

import java.io.Serializable;

public class PsnInfo implements Serializable {


  private static final long serialVersionUID = -9086474457478862209L;
  private Long psnId;// 人员Id
  private String des3PsnId;// 加密的人员Id
  private String psnName;// 人员姓名
  private String firstName;// 人员姓
  private String lastName;// 人员名
  private Long unitId;// 单位Id
  private String unitName;// 单位名称
  private String degree;// 职称
  private String avatarsUrl;// 头像地址
  private String insAndDep; // 人员单位和部门信息
  private String posAndTitolo; // 职称和头衔信息
  private String regData;

  private Long insId; // 机构Id
  private String insName; // 机构名称
  private String email; // 邮箱
  private String loginName;// 人员登录账号
  private String lastLoginTime;// 最近登录时间
  private Integer pubSum;// 成果数
  private Integer prjSum;// 项目数
  private Integer patentSum;// 专利数
  private String psnUrl;// 人员首页的url
  private Integer isCheckEmail = 0; // 是否查看邮件

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

  public String getAvatarsUrl() {
    return avatarsUrl;
  }

  public void setAvatarsUrl(String avatarsUrl) {
    this.avatarsUrl = avatarsUrl;
  }

  public String getInsAndDep() {
    return insAndDep;
  }

  public void setInsAndDep(String insAndDep) {
    this.insAndDep = insAndDep;
  }

  public String getPosAndTitolo() {
    return posAndTitolo;
  }

  public void setPosAndTitolo(String posAndTitolo) {
    this.posAndTitolo = posAndTitolo;
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

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(String lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getPatentSum() {
    return patentSum;
  }

  public void setPatentSum(Integer patentSum) {
    this.patentSum = patentSum;
  }

  public String getPsnUrl() {
    return psnUrl;
  }

  public void setPsnUrl(String psnUrl) {
    this.psnUrl = psnUrl;
  }

  public String getRegData() {
    return regData;
  }

  public void setRegData(String regData) {
    this.regData = regData;
  }

  public Integer getIsCheckEmail() {
    return isCheckEmail;
  }

  public void setIsCheckEmail(Integer isCheckEmail) {
    this.isCheckEmail = isCheckEmail;
  }


}
