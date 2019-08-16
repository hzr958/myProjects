package com.smate.web.prj.form.fileimport;

import java.io.Serializable;

/**
 * 项目成员
 *
 * @author aijiangbin
 * @create 2019-08-03 15:30
 **/
public class PrjMemberDTO implements Serializable {

  private static final long serialVersionUID = -9088855685272731461L;
  private String prjNo ="";  //批准号
  private String seqNo =""; //序号
  private String name =""; //姓名
  private String email =""; //邮箱
  private String mobile =""; //手机号（11位数字）
  private String openId =""; //科研之友编号（即openId）
  private String insName =""; //所在单位名称
  private String isLeader =""; //是否负责人（是 或 否）

  public String getPrjNo() {
    return prjNo;
  }

  public void setPrjNo(String prjNo) {
    this.prjNo = prjNo;
  }

  public String getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getIsLeader() {
    return isLeader;
  }

  public void setIsLeader(String isLeader) {
    this.isLeader = isLeader;
  }
}
