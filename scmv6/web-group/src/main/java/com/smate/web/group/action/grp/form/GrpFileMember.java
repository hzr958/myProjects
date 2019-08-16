package com.smate.web.group.action.grp.form;

/**
 * 群组文件成员
 * 
 * @author AiJiangBin
 *
 */
public class GrpFileMember {

  private String des3MemberId; // 加密的成员id
  private String memberName; // 姓名
  private String memberAvator; // 头像
  private Integer memberFileNum; // 组员文件数

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getMemberAvator() {
    return memberAvator;
  }

  public void setMemberAvator(String memberAvator) {
    this.memberAvator = memberAvator;
  }

  public Integer getMemberFileNum() {
    return memberFileNum;
  }

  public void setMemberFileNum(Integer memberFileNum) {
    this.memberFileNum = memberFileNum;
  }

  public String getDes3MemberId() {
    return des3MemberId;
  }

  public void setDes3MemberId(String des3MemberId) {
    this.des3MemberId = des3MemberId;
  }

}
