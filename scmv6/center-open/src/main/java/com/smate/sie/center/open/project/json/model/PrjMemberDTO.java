package com.smate.sie.center.open.project.json.model;

import java.io.Serializable;

/**
 * 项目成员
 * 
 * @author lijianming
 * @date 2019年6月3日
 *
 */
public class PrjMemberDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4718386580230581879L;

  private Long pmId; // 成员表主键

  private Integer seqNo; // 序号

  private String name; // 名称

  private Long psnId; // 成员id

  private String email; // 邮箱

  private boolean notifyAuthor = false; // 是否可联系的，是否是通讯作者

  private Long insId;

  private String insName; // 单位名称

  private boolean firstAuthor = false; // 是否第一作者

  /**
   * 成员id
   * 
   * @return
   */
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * 排序号
   * 
   * @return
   */
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  /**
   * 成员名称
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * 成员邮件地址
   * 
   * @return
   */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * 是否可联系，是否是通讯作者
   * 
   * @return
   */
  public boolean isNotifyAuthor() {
    return notifyAuthor;
  }

  public void setNotifyAuthor(boolean notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }

  /**
   * 是否第一作者
   * 
   * @return
   */
  public boolean isFirstAuthor() {
    return firstAuthor;
  }

  public void setFirstAuthor(boolean firstAuthor) {
    this.firstAuthor = firstAuthor;
  }

  public Long getPmId() {
    return pmId;
  }

  public Long getInsId() {
    return insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Override
  public String toString() {
    return "PubMemberBean [pmId=" + pmId + ", psnId=" + psnId + ", seqNo=" + seqNo + ", name=" + name + ", email="
        + email + ", notifyAuthor=" + notifyAuthor + ", insId=" + insId + ", insName=" + insName + ", firstAuthor="
        + firstAuthor + "]";
  }
}
