package com.smate.web.v8pub.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 成果成员
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
public class PubMemberBean implements Serializable {

  private static final long serialVersionUID = -1918453346196120626L;

  private Long memberId; // member表中的memberid

  private Long psnId; // 成员id

  private Integer seqNo; // 序号

  private String name = new String(); // 名称

  private String email = new String(); // 邮箱

  private String dept; // 作者单位

  private boolean communicable = false; // 是否可联系的，是否是通讯作者

  private List<MemberInsBean> insNames = new ArrayList<>(); // 单位名称

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
   * 成员单位
   * 
   * @return
   */
  public List<MemberInsBean> getInsNames() {
    return insNames;
  }

  public void setInsNames(List<MemberInsBean> insNames) {
    this.insNames = insNames;
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

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  @Override
  public String toString() {
    return "PubMemberBean{" + "psnId=" + psnId + ", seqNo=" + seqNo + ", name='" + name + '\'' + ", email='" + email
        + '\'' + ", communicable='" + communicable + '\'' + ", insName='" + insNames + '\'' + ", firstAuthor="
        + firstAuthor + '}';
  }
}
