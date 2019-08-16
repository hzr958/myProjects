package com.smate.web.group.model.grp.member;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 申请中成员关系实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GRP_PROPOSER")
public class GrpProposer implements Serializable {

  private static final long serialVersionUID = -5557842611418196557L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_PROPOSER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "GRP_ID")
  private Long grpId;// 群组Id
  @Column(name = "PSN_ID")
  private Long psnId;// 申请中成员Id
  @Column(name = "IS_ACCEPT")
  private Integer isAccept;// 是否同意加入群组（2=待确认,1=已确认,0=否,-1=取消申请）
  @Column(name = "TYPE")
  private Integer type;// 加入方式（1=申请加入，2=被邀请加入）
  @Column(name = "INVITER_ID")
  private Long inviterId;// 邀请人Id
  @Column(name = "APPROVER_ID")
  private Long approverId;// 批准人Id（如果type=2,这里是被邀请人Id）
  @Column(name = "APPROVER_DATE")
  private Date approverDate;// 批准时间
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  public GrpProposer() {}

  public GrpProposer(Long id, Long grpId, Long inviterId) {
    super();
    this.id = id;
    this.grpId = grpId;
    this.inviterId = inviterId;
  }

  public GrpProposer(Long id, Long psnId) {
    super();
    this.id = id;
    this.psnId = psnId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getIsAccept() {
    return isAccept;
  }

  public void setIsAccept(Integer isAccept) {
    this.isAccept = isAccept;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getInviterId() {
    return inviterId;
  }

  public void setInviterId(Long inviterId) {
    this.inviterId = inviterId;
  }

  public Long getApproverId() {
    return approverId;
  }

  public void setApproverId(Long approverId) {
    this.approverId = approverId;
  }

  public Date getApproverDate() {
    return approverDate;
  }

  public void setApproverDate(Date approverDate) {
    this.approverDate = approverDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
