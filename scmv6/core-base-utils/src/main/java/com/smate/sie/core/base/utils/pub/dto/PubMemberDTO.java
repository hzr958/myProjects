package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 成果成员
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public class PubMemberDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 747241461711048666L;

  private Long pmId; // 成员表主键

  private Long psnId; // 成员id

  private Integer seqNo; // 序号

  private String name; // 名称

  private String email; // 邮箱

  private boolean communicable = false; // 是否可联系的，是否是通讯作者

  private Long insId;

  private String insName; // 单位名称

  private List<MemberInsDTO> insNames; // 单位名称，多单位

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
  public boolean isCommunicable() {
    return communicable;
  }

  public void setCommunicable(boolean communicable) {
    this.communicable = communicable;
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

  public List<MemberInsDTO> getInsNames() {
    return insNames;
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

  public void setInsNames(List<MemberInsDTO> insNames) {
    this.insNames = insNames;
  }

  @Override
  public String toString() {
    return "PubMemberDTO [pmId=" + pmId + ", psnId=" + psnId + ", seqNo=" + seqNo + ", name=" + name + ", email="
        + email + ", communicable=" + communicable + ", insId=" + insId + ", insNames=" + insNames + ", firstAuthor="
        + firstAuthor + "]";
  }

}
