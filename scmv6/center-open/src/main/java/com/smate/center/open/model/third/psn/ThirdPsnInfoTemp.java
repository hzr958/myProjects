package com.smate.center.open.model.third.psn;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 第三方人员信息 临时表
 * 
 * @author AiJiangBin
 *
 */

public class ThirdPsnInfoTemp {

  private String name;
  private String email;
  private String insName;
  private String department;
  private String position;
  private String degree;
  private String disciplineCodes;
  private String researchArea;
  private String country;
  private String province;
  private String city;
  private String birthdate;
  private String researchDirection;
  private String keywordsZh;
  private String keywordsEn;


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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }



  public String getResearchArea() {
    return researchArea;
  }

  public void setResearchArea(String researchArea) {
    this.researchArea = researchArea;
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

  public String getResearchDirection() {
    return researchDirection;
  }

  public void setResearchDirection(String researchDirection) {
    this.researchDirection = researchDirection;
  }

  public String getKeywordsZh() {
    return keywordsZh;
  }

  public void setKeywordsZh(String keywordsZh) {
    this.keywordsZh = keywordsZh;
  }

  public String getKeywordsEn() {
    return keywordsEn;
  }

  public void setKeywordsEn(String keywordsEn) {
    this.keywordsEn = keywordsEn;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDisciplineCodes() {
    return disciplineCodes;
  }

  public void setDisciplineCodes(String disciplineCodes) {
    this.disciplineCodes = disciplineCodes;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
  }



}
