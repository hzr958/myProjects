package com.smate.web.psn.form;



import java.io.Serializable;

/**
 * 人员合并帐号的form表单.
 * 
 * @author mjg
 * 
 */
public class MergeCountForm implements Serializable {

  private static final long serialVersionUID = 8200115230157755109L;
  private Long psnId;// 人员ID.
  private String psnDes3Id;
  private String psnAvatars;// 人员头像.
  private String psnZhName;// 人员中文名.
  private String psnFirstName;// 英文名.
  private String psnLastName;// 英文姓.
  private String psnViewName;// 人员显示名.
  private String psnTitolo;// 人员头衔.
  private String loginCount;// 登录帐号.
  private String psnEmail;// 人员首要邮件地址.

  public MergeCountForm() {}

  public MergeCountForm(Long psnId, String psnDes3Id, String psnAvatars, String psnZhName, String psnFirstName,
      String psnLastName, String psnViewName, String psnTitolo, String loginCount, String psnEmail) {
    super();
    this.psnId = psnId;
    this.psnDes3Id = psnDes3Id;
    this.psnAvatars = psnAvatars;
    this.psnZhName = psnZhName;
    this.psnFirstName = psnFirstName;
    this.psnLastName = psnLastName;
    this.psnViewName = psnViewName;
    this.psnTitolo = psnTitolo;
    this.loginCount = loginCount;
    this.psnEmail = psnEmail;
  }

  public Long getPsnId() {
    return psnId;
  }

  public String getPsnZhName() {
    return psnZhName;
  }

  public String getPsnFirstName() {
    return psnFirstName;
  }

  public String getPsnLastName() {
    return psnLastName;
  }

  public String getPsnTitolo() {
    return psnTitolo;
  }

  public String getLoginCount() {
    return loginCount;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPsnZhName(String psnZhName) {
    this.psnZhName = psnZhName;
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

  public void setLoginCount(String loginCount) {
    this.loginCount = loginCount;
  }

  public String getPsnDes3Id() {
    return psnDes3Id;
  }

  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnDes3Id(String psnDes3Id) {
    this.psnDes3Id = psnDes3Id;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public String getPsnViewName() {
    return psnViewName;
  }

  public void setPsnViewName(String psnViewName) {
    this.psnViewName = psnViewName;
  }

  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

}
