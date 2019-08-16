package com.smate.web.management.model.mail.bpo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IRISSZ_MAIL_DETAIL")
public class IrisszMail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7910110494174059760L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IRISSZ_MAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "CLIECT_IP")
  private String cliectIP;
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LAST_NAME")
  private String lastName;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "TEL")
  private String tel;
  @Column(name = "INS_NAME")
  private String insName;
  @Column(name = "INS_SCALE")
  private String insScale;
  @Column(name = "POSITION")
  private String position;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "CITY")
  private String city;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "REMARK")
  private String remark;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "DOMAIN")
  private String domain;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCliectIP() {
    return cliectIP;
  }

  public void setCliectIP(String cliectIP) {
    this.cliectIP = cliectIP;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getInsScale() {
    return insScale;
  }

  public void setInsScale(String insScale) {
    this.insScale = insScale;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }


}
