package com.smate.web.psn.form;

public class AttPersonInfo {



  private Long attPersonId; // 关注id 主键
  private String refDes3PsnId; // 被我关注人的psnid
  private String refName; // 被我关人员的名字
  private String refInsName; // 被我单位名称
  private String refTitolo; // 被我关注的人员头衔
  private String refHeadUrl; // 被我关注人的头像



  public Long getAttPersonId() {
    return attPersonId;
  }


  public void setAttPersonId(Long attPersonId) {
    this.attPersonId = attPersonId;
  }


  public void setRefDes3PsnId(String refDes3PsnId) {
    this.refDes3PsnId = refDes3PsnId;
  }


  public String getRefDes3PsnId() {
    return refDes3PsnId;
  }


  public String getRefName() {
    return refName;
  }

  public void setRefName(String refName) {
    this.refName = refName;
  }

  public String getRefInsName() {
    return refInsName;
  }

  public void setRefInsName(String refInsName) {
    this.refInsName = refInsName;
  }

  public String getRefHeadUrl() {
    return refHeadUrl;
  }

  public void setRefHeadUrl(String refHeadUrl) {
    this.refHeadUrl = refHeadUrl;
  }


  public String getRefTitolo() {
    return refTitolo;
  }


  public void setRefTitolo(String refTitolo) {
    this.refTitolo = refTitolo;
  }



}
