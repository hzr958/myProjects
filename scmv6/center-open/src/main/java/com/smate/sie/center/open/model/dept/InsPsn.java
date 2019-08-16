package com.smate.sie.center.open.model.dept;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PSN_INS")
public class InsPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3605098688627190875L;

  @Id
  @Column(name = "PSN_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_INS", allocationSize = 1)
  private Long psn_id;

  @Column(name = "INS_ID")
  private Long insId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "ZH_NAME")
  private String zhName;

  @Column(name = "PSN_EMAIL")
  private String psnEmail;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "TITLE")
  private String title;

  public Long getPsn_id() {
    return psn_id;
  }

  public void setPsn_id(Long psn_id) {
    this.psn_id = psn_id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

}
