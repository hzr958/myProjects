package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 导出好友.
 * 
 * @author lichangwen
 * 
 */
public class ExportFriend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6284548236835048805L;
  private String friendName;
  private String friendTitle;
  private String friendEmail;
  private String friendTel;
  private String friendMobile;
  private String friendQQ;
  private String friendMsn;
  private String friendPrjNum;
  private String friendPubNum;
  private String friendISI;
  private String friendHindex;
  private String friendDiscipline;
  private String friendGroupList;
  private String friendSkype;

  public ExportFriend() {
    super();
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getFriendName() {
    return friendName == null ? "" : friendName;
  }

  public void setFriendName(String friendName) {
    this.friendName = friendName;
  }

  public String getFriendTitle() {
    return friendTitle == null ? "" : friendTitle;
  }

  public void setFriendTitle(String friendTitle) {
    this.friendTitle = friendTitle;
  }

  public String getFriendEmail() {
    return friendEmail == null ? "" : friendEmail;
  }

  public void setFriendEmail(String friendEmail) {
    this.friendEmail = friendEmail;
  }

  public String getFriendTel() {
    return friendTel == null ? "" : friendTel;
  }

  public void setFriendTel(String friendTel) {
    this.friendTel = friendTel;
  }

  public String getFriendMobile() {
    return friendMobile == null ? "" : friendMobile;
  }

  public void setFriendMobile(String friendMobile) {
    this.friendMobile = friendMobile;
  }

  public String getFriendQQ() {
    return friendQQ == null ? "" : friendQQ;
  }

  public void setFriendQQ(String friendQQ) {
    this.friendQQ = friendQQ;
  }

  public String getFriendMsn() {
    return friendMsn == null ? "" : friendMsn;
  }

  public void setFriendMsn(String friendMsn) {
    this.friendMsn = friendMsn;
  }

  public String getFriendGroupList() {
    return friendGroupList == null ? "" : friendGroupList;
  }

  public void setFriendGroupList(String friendGroupList) {
    this.friendGroupList = friendGroupList;
  }

  public String getFriendPrjNum() {
    return friendPrjNum;
  }

  public void setFriendPrjNum(String friendPrjNum) {
    this.friendPrjNum = friendPrjNum;
  }

  public String getFriendPubNum() {
    return friendPubNum;
  }

  public void setFriendPubNum(String friendPubNum) {
    this.friendPubNum = friendPubNum;
  }

  public String getFriendISI() {
    return friendISI;
  }

  public void setFriendISI(String friendISI) {
    this.friendISI = friendISI;
  }

  public String getFriendHindex() {
    return friendHindex;
  }

  public void setFriendHindex(String friendHindex) {
    this.friendHindex = friendHindex;
  }

  public String getFriendDiscipline() {
    return friendDiscipline;
  }

  public void setFriendDiscipline(String friendDiscipline) {
    this.friendDiscipline = friendDiscipline;
  }

  public String getFriendSkype() {
    return friendSkype == null ? "" : friendSkype;
  }

  public void setFriendSkype(String friendSkype) {
    this.friendSkype = friendSkype;
  }

}
