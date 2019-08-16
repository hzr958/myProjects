package com.smate.web.v8pub.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 成果成员
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubMemberDTO implements Serializable {

  private static final long serialVersionUID = -1918453346196120626L;

  private Long memberId; // V_PUB_PDWH_MEMBER的主键

  private Long psnId; // 成员id

  private String des3PsnId; // 加密的成员id

  private Integer seqNo; // 序号

  private String name; // 名称

  private String abbrName;// 简称

  private String email; // 邮箱

  private String dept; // 作者单位

  private Set<String> depts = new HashSet<>(); // 单位数组

  private boolean communicable; // 是否可联系的，是否是通讯作者

  private boolean firstAuthor; // 是否第一作者

  private boolean owner; // 是否是拥有者

  private List<MemberInsDTO> insNames; // 单位名称

  private Integer matchCount = 0; // 匹配上pub_author内的作者数量

  /**
   * 成员id
   * 
   * @return
   */
  public Long getPsnId() {
    return DisposeDes3IdUtils.disposeDes3Id(psnId, des3PsnId);
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
    if (!NumberUtils.isNullOrZero(psnId)) {
      this.des3PsnId = Des3Utils.encodeToDes3(String.valueOf(psnId));
    }
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
  public List<MemberInsDTO> getInsNames() {
    return insNames;
  }

  public void setInsNames(List<MemberInsDTO> insNames) {
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

  public boolean isOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public Set<String> getDepts() {
    return depts;
  }

  public void setDepts(Set<String> depts) {
    this.depts = depts;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public Integer getMatchCount() {
    return matchCount;
  }

  public void setMatchCount(Integer matchCount) {
    this.matchCount = matchCount;
  }


  public String getAbbrName() {
    return abbrName;
  }

  public void setAbbrName(String abbrName) {
    this.abbrName = abbrName;
  }

  @Override
  public String toString() {
    return "PdwhPubMemberDTO{" + "psnId=" + psnId + ", seqNo=" + seqNo + ", name='" + name + '\'' + ", email='" + email
        + '\'' + ", communicable='" + communicable + '\'' + ", dept='" + dept + '\'' + ", firstAuthor=" + firstAuthor
        + '}';
  }
}
