package com.smate.sie.center.open.project.json.model;

import java.io.Serializable;

/**
 * 项目组成员
 * 
 * @author lijianming
 * 
 */
public class PrjMemberBean implements Serializable {

  private static final long serialVersionUID = 3039227031536696983L;

  private Long pmId; // 成员表主键

  private Integer seqNo; // 序号

  private Long psnId; // 成员id

  private String name = new String(); // 名称

  private String email = new String(); // 邮箱

  private boolean notifyAuthor = false; // 是否可联系的，是否是通讯作者

  private Long insId;

  private String insName = new String(); // 单位名称

  private boolean firstAuthor = false; // 是否第一作者

  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  public boolean isNotifyAuthor() {
    return notifyAuthor;
  }

  public void setNotifyAuthor(boolean notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public boolean isFirstAuthor() {
    return firstAuthor;
  }

  public void setFirstAuthor(boolean firstAuthor) {
    this.firstAuthor = firstAuthor;
  }

  @Override
  public String toString() {
    return "PrjMemberBean [pmId=" + pmId + ", seqNo=" + seqNo + ", psnId=" + psnId + ", name=" + name + ", email="
        + email + ", notifyAuthor=" + notifyAuthor + ", insId=" + insId + ", insName=" + insName + ", firstAuthor="
        + firstAuthor + "]";
  }
}
