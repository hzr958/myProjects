package com.smate.web.group.action.grp.form;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组讨论显示成员信息
 * 
 * @author AiJiangBin
 *
 */
public class GrpDiscussShowMemberInfo {
  private Long memberId;
  private String des3MemberId;
  private String memberName; // 成员名字
  private String memberAvatur; // 成员头像
  private Boolean isFriend = false;// 是否是好友
  private String insName;// 单位名称
  private Integer pubNum;// 成果数

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getMemberAvatur() {
    return memberAvatur;
  }

  public void setMemberAvatur(String memberAvatur) {
    this.memberAvatur = memberAvatur;
  }

  public Boolean getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Boolean isFriend) {
    this.isFriend = isFriend;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Integer getPubNum() {
    return pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  public String getDes3MemberId() {
    if (StringUtils.isBlank(des3MemberId) && memberId != null) {
      des3MemberId = Des3Utils.encodeToDes3(memberId.toString());
    }
    return des3MemberId;
  }

  public void setDes3MemberId(String des3MemberId) {
    this.des3MemberId = des3MemberId;
  }

}
