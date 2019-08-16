package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ROL成果/人员信息.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PUB_CONFIRM_ROLPUB_MEMBER")
public class PubConfirmRolPubMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8900454530171537867L;
  // 主键唯一标识
  private Long pmId;
  private Long rolPubId;
  // 单位PMid
  private Long insPmId;
  // Sequence no for display
  private Integer seqNo;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id
  private Long psnId;

  public PubConfirmRolPubMember() {
    super();
  }

  public PubConfirmRolPubMember(Long pmId, Long rolPubId, Long insPmId, Integer seqNo, String name, Long psnId) {
    super();
    this.pmId = pmId;
    this.rolPubId = rolPubId;
    this.insPmId = insPmId;
    this.seqNo = seqNo;
    this.name = name;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "PM_ID")
  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  @Column(name = "ROLPUB_ID")
  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  @Column(name = "INS_PM_ID")
  public Long getInsPmId() {
    return insPmId;
  }

  public void setInsPmId(Long insPmId) {
    this.insPmId = insPmId;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "MEMBER_NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "MEMBER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {

    this.psnId = psnId;
  }

}
