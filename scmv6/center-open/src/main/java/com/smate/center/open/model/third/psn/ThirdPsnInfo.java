package com.smate.center.open.model.third.psn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 第三方人员信息
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "THIRD_SYS_PSN_INFO")
public class ThirdPsnInfo {
  /**
   * 
   */
  private static final long serialVersionUID = -249848045024635765L;
  private Long id;
  private Long psnId;
  private String fromSys;
  private String name;
  private String email;
  private String insName;
  private String department;
  private String position;
  private String degree;
  private String disciplineCodes;
  private String researchDirection;
  private String keywordsZh;
  private String keywordsEn;
  private String country;
  private String province;
  private String city;
  private String birthdate;


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_THIRD_SYS_PSN_INFO", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FROM_SYS")
  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Column(name = "DEGREE")
  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }



  @Column(name = "COUNTRY")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  @Column(name = "PROVINCE")
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(name = "BIRTHDATE")
  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
  }

  @Column(name = "RESEARCH_DIRECTION")
  public String getResearchDirection() {
    return researchDirection;
  }

  public void setResearchDirection(String researchDirection) {
    this.researchDirection = researchDirection;
  }

  @Column(name = "KEYWORDS_ZH")
  public String getKeywordsZh() {
    return keywordsZh;
  }

  public void setKeywordsZh(String keywordsZh) {
    this.keywordsZh = keywordsZh;
  }

  @Column(name = "KEYWORDS_EN")
  public String getKeywordsEn() {
    return keywordsEn;
  }

  public void setKeywordsEn(String keywordsEn) {
    this.keywordsEn = keywordsEn;
  }

  @Column(name = "DISCIPLINE_CODES")
  public String getDisciplineCodes() {
    return disciplineCodes;
  }

  public void setDisciplineCodes(String disciplineCodes) {
    this.disciplineCodes = disciplineCodes;
  }

}
