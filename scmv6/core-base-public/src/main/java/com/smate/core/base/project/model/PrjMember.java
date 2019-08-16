package com.smate.core.base.project.model;

import com.smate.core.base.utils.string.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 项目/人员信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_MEMBER")
public class PrjMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2007998956408962364L;
  // 主键唯一标识
  private Long id;
  // 项目ID
  private Long prjId;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id （该成员通过查找选择本机构人员进行添加）
  private Long psnId;
  // 1: 为本人，0: 不是本人
  private Integer owner;
  // 作者所在单位个数，单位详细存放在XML
  private Integer insCount;
  // EMAIL
  private String email;
  // 项目负责人0/1
  private Integer notifyAuthor;
  // 顺序
  private Integer seqNo;

  @Id
  @Column(name = "PM_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_MEMBER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  @Column(name = "MEMBER_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "MEMBER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "OWNER")
  public Integer getOwner() {
    return owner;
  }

  @Column(name = "INS_COUNT")
  public Integer getInsCount() {
    return insCount;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "NOTIFY_AUTHOR")
  public Integer getNotifyAuthor() {
    return notifyAuthor;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setOwner(Integer owner) {
    this.owner = owner;
  }

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  public void setEmail(String email) {
    email = StringUtils.subMaxLengthString(email,50);
    this.email = email;
  }

  public void setNotifyAuthor(Integer notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }

}
