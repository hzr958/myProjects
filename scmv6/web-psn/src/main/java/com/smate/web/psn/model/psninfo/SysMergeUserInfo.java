package com.smate.web.psn.model.psninfo;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 被合并删除的人员信息表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "SYS_MERGE_USER_INFO")
public class SysMergeUserInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 514774525187250026L;
  private Long id;
  private Long psnId;
  private Long delPsnId;
  private String delDesPsnId;
  // 以下字段均为被删除人的信息.
  private String psnAvatars;
  private String psnZhName;
  private String psnEnName;
  private String psnFirstName;
  private String psnLastName;
  private String psnTitolo;
  private String psnEmail;
  private String insName;
  private String loginCount;
  private String psnViewName;
  private String psnViewTitolo;

  public SysMergeUserInfo() {
    super();
  }

  public SysMergeUserInfo(Long id, Long psnId, Long delPsnId, String delDesPsnId, String psnAvatars, String psnZhName,
      String psnEnName, String psnFirstName, String psnLastName, String psnTitolo, String psnEmail, String insName,
      String loginCount) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.delPsnId = delPsnId;
    this.delDesPsnId = delDesPsnId;
    this.psnAvatars = psnAvatars;
    this.psnZhName = psnZhName;
    this.psnEnName = psnEnName;
    this.psnFirstName = psnFirstName;
    this.psnLastName = psnLastName;
    this.psnTitolo = psnTitolo;
    this.psnEmail = psnEmail;
    this.insName = insName;
    this.loginCount = loginCount;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_MERGE_USER_INFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "DEL_PSN_ID")
  public Long getDelPsnId() {
    return delPsnId;
  }

  @Column(name = "DEL_DES3_PSN_ID")
  public String getDelDesPsnId() {
    return delDesPsnId;
  }

  @Column(name = "AVATARS")
  public String getPsnAvatars() {
    return psnAvatars;
  }

  @Column(name = "ZH_NAME")
  public String getPsnZhName() {
    return psnZhName;
  }

  @Column(name = "EN_NAME")
  public String getPsnEnName() {
    return psnEnName;
  }

  @Column(name = "FIRST_NAME")
  public String getPsnFirstName() {
    return psnFirstName;
  }

  @Column(name = "LAST_NAME")
  public String getPsnLastName() {
    return psnLastName;
  }

  @Column(name = "TITOLO")
  public String getPsnTitolo() {
    return psnTitolo;
  }

  @Column(name = "EMAIL")
  public String getPsnEmail() {
    return psnEmail;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "LOGIN_COUNT")
  public String getLoginCount() {
    return loginCount;
  }

  @Column(name = "VIEW_NAME")
  public String getPsnViewName() {
    return psnViewName;
  }

  @Column(name = "VIEW_TITOLO")
  public String getPsnViewTitolo() {
    return psnViewTitolo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setDelPsnId(Long delPsnId) {
    this.delPsnId = delPsnId;
  }

  public void setDelDesPsnId(String delDesPsnId) {
    this.delDesPsnId = delDesPsnId;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public void setPsnZhName(String psnZhName) {
    this.psnZhName = psnZhName;
  }

  public void setPsnEnName(String psnEnName) {
    this.psnEnName = psnEnName;
  }

  public void setPsnFirstName(String psnFirstName) {
    this.psnFirstName = psnFirstName;
  }

  public void setPsnLastName(String psnLastName) {
    this.psnLastName = psnLastName;
  }

  public void setPsnTitolo(String psnTitolo) {
    this.psnTitolo = psnTitolo;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setLoginCount(String loginCount) {
    this.loginCount = loginCount;
  }

  public void setPsnViewName(String psnViewName) {
    this.psnViewName = psnViewName;
  }

  public void setPsnViewTitolo(String psnViewTitolo) {
    this.psnViewTitolo = psnViewTitolo;
  }
}
