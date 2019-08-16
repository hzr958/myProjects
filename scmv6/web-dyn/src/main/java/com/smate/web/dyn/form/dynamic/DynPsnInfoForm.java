package com.smate.web.dyn.form.dynamic;

import java.util.List;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;

public class DynPsnInfoForm {
  private Long psnId; // 人员id
  private String des3PsnId; // 人员加密id
  private String name; // 人员显示姓名
  private String zhName; // 人员显示姓名
  private String EnName; // 人员显示姓名
  private String insInfo; // 首要单位信息及职称
  private String avatarUrl; // 人员头像地址
  private String profileUrl; // 人员站外地址
  private String insAndDep; // 人员单位和部门信息
  private String posAndTitolo; // 职称和头衔信息
  private List<PsnDisciplineKey> discList; // 人员关键词list
  private boolean isFriend; // 是否是好友
  private String psnShortUrl;
  private String email;// 邮箱

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return EnName;
  }

  public void setEnName(String enName) {
    EnName = enName;
  }

  public String getInsInfo() {
    return insInfo;
  }

  public void setInsInfo(String insInfo) {
    this.insInfo = insInfo;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
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

  public List<PsnDisciplineKey> getDiscList() {
    return discList;
  }

  public void setDiscList(List<PsnDisciplineKey> discList) {
    this.discList = discList;
  }

  public boolean isFriend() {
    return isFriend;
  }

  public void setFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public String getPsnShortUrl() {
    return psnShortUrl;
  }

  public void setPsnShortUrl(String psnShortUrl) {
    this.psnShortUrl = psnShortUrl;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
