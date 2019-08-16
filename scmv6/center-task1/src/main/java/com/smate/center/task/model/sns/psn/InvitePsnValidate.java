package com.smate.center.task.model.sns.psn;

import java.io.Serializable;
/**
 * 邀请订购科研验证人员表
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INVITE_PSN_VALIDATE")
public class InvitePsnValidate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4522642069608014226L;
  private Long psnId;
  private String insName;
  private String name;
  private String email;
  private String titolo;
  private String position;
  private String degreename;
  private String birthday;
  private String psnIndexUrl;
  private String flag2;
  private String flag;
  private Integer sendStatus;

  public InvitePsnValidate() {
    super();
  }

  public InvitePsnValidate(String name, String email, String psnIndexUrl) {
    super();
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
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

  @Column(name = "TITOLO")
  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Column(name = "DEGREE_NAME")
  public String getDegreename() {
    return degreename;
  }

  public void setDegreename(String degreename) {
    this.degreename = degreename;
  }

  @Column(name = "BIRTHDAY")
  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  @Column(name = "PSN_INDEX_URL")
  public String getPsnIndexUrl() {
    return psnIndexUrl;
  }

  public void setPsnIndexUrl(String psnIndexUrl) {
    this.psnIndexUrl = psnIndexUrl;
  }

  @Column(name = "FLAG2")
  public String getFlag2() {
    return flag2;
  }

  public void setFlag2(String flag2) {
    this.flag2 = flag2;
  }

  @Column(name = "FLAG")
  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  @Column(name = "SEND_STATUS")
  public Integer getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }



}
