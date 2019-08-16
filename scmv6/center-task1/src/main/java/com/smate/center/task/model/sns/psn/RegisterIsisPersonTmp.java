package com.smate.center.task.model.sns.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
/**
 * isis注册人员实体类
 * 
 * @author zzx
 *
 */
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTER_ISIS_PERSON_TMP")
public class RegisterIsisPersonTmp implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ISIS_PSN_ID")
  private Long isisPsnId;// isis人员id
  @Column(name = "NEME")
  private String name;// 名字
  @Column(name = "EMAIL")
  private String email;// 邮件
  @Column(name = "FIRST_NAME")
  private String firstName;// firstName
  @Column(name = "LAST_NAME")
  private String lastName;// lastName
  @Column(name = "POSITION")
  private String position;// 职业
  @Column(name = "INS_NAME")
  private String insName;// 机构名字
  @Column(name = "DEPARTMENT")
  private String department;// 单位
  @Column(name = "DEGREE")
  private String degree;// 学位
  @Column(name = "COUNTRY")
  private String country;// 国家
  @Column(name = "PROVINCE")
  private String province;// 省
  @Column(name = "CITY")
  private String city;// 城市
  @Column(name = "BIRTHDATE")
  private String birthDate;// 出生日期
  @Column(name = "FROM_TOKEN")
  private String fromToken;// 来源token
  @Column(name = "DEAL_STATUS")
  private Integer dealStatus;// 处理状态 0=未处理；1=已处理；2=处理失败
  @Column(name = "SCM_OPEN_ID")
  private Long scmOpenId;// scmopenId

  public Long getIsisPsnId() {
    return isisPsnId;
  }

  public void setIsisPsnId(Long isisPsnId) {
    this.isisPsnId = isisPsnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getFromToken() {
    return fromToken;
  }

  public void setFromToken(String fromToken) {
    this.fromToken = fromToken;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }


}
