package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 项目/人员信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_MEMBER")
public class RolPrjMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7242863448661518568L;
  // 主键唯一标识
  private Long id;
  // 项目ID
  private Long prjId;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id （该成员通过查找选择本机构人员进行添加）
  private Long psnId;
  // 作废.
  private Integer insCount;
  // EMAIL
  private String email;
  // 项目负责人0/1
  private Integer notifyAuthor;
  // 顺序
  private Integer seqNo;
  // 项目所属单位，冗余
  private Long insId;
  // 成果作者机构ID。可能是本机构；也是能是其他机构；如果地址未确定机构，
  // 则设置为1；地址空则设置为空，如果匹配上了作者，设置为本机构，否则为空.
  private Long pmInsId;

  private String showPsnName;// 显示的人员名称(包括单位)_MJG_ROL-936.

  public RolPrjMember() {
    super();
  }

  public RolPrjMember(Long prjId, String name, Long psnId, Integer notifyAuthor, Integer seqNo, Long insId,
      Long pmInsId) {
    super();
    this.prjId = prjId;
    this.name = name;
    this.psnId = psnId;
    this.notifyAuthor = notifyAuthor;
    this.seqNo = seqNo;
    this.insId = insId;
    this.pmInsId = pmInsId;
  }

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

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PM_INS_ID")
  public Long getPmInsId() {
    return pmInsId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPmInsId(Long pmInsId) {
    this.pmInsId = pmInsId;
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

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Transient
  public String getShowPsnName() {
    return showPsnName;
  }

  public void setShowPsnName(String showPsnName) {
    this.showPsnName = showPsnName;
  }

  public void setNotifyAuthor(Integer notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }

}
