package com.smate.center.task.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON_FL_NAME")
public class PersonFLName {
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "NAME_ZH")
  private String nameZh;
  @Column(name = "FIRST_NAME_EN")
  private String firstNameEN;

  @Column(name = "LAST_NAME_EN")
  private String lastNameEN;

  @Column(name = "NAME_EN")
  private String nameEn;

  @Column(name = "FIRST_NAME_ZH")
  private String firstNameZh;

  @Column(name = "LAST_NAME_ZH")
  private String lastNameZh;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  public String getFirstNameEN() {
    return firstNameEN;
  }

  public void setFirstNameEN(String firstNameEN) {
    this.firstNameEN = firstNameEN;
  }

  public String getLastNameEN() {
    return lastNameEN;
  }

  public void setLastNameEN(String lastNameEN) {
    this.lastNameEN = lastNameEN;
  }

  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  public String getFirstNameZh() {
    return firstNameZh;
  }

  public void setFirstNameZh(String firstNameZh) {
    this.firstNameZh = firstNameZh;
  }

  public String getLastNameZh() {
    return lastNameZh;
  }

  public void setLastNameZh(String lastNameZh) {
    this.lastNameZh = lastNameZh;
  }

  public PersonFLName() {
    super();
  }

  public PersonFLName(Long psnId, String nameZh, String firstNameEN, String lastNameEN, String nameEn,
      String firstNameZh, String lastNameZh) {
    super();
    this.psnId = psnId;
    this.nameZh = nameZh;
    this.firstNameEN = firstNameEN;
    this.lastNameEN = lastNameEN;
    this.nameEn = nameEn;
    this.firstNameZh = firstNameZh;
    this.lastNameZh = lastNameZh;
  }

}
