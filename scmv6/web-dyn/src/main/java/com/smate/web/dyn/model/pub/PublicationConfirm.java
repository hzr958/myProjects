package com.smate.web.dyn.model.pub;



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
 * 成果确认实体.
 * 
 * @author LY
 * 
 */
@Entity
@Table(name = "PUB_CONFIRM")
public class PublicationConfirm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3984296519437728761L;
  private Long id;
  private Long psnId;
  private Long insId;
  private Date confirmDate;
  private Integer confirmResult;
  private Integer assignSeqNo;
  private Integer confirmSeqNo;
  private Long rolPubId;
  private Long insAssignId;
  private Long pmId;
  // 0:未操作；1:没有重复成果；2：覆盖；3：新增；
  private Integer dupStatus;
  // 0:未操作；1：确认成功。2：确认失败，该人不在此单位。3：确认失败，单位端已经把该人的匹配删除
  private Integer syncStatus;
  private String dupPubIds;
  private Float assignScore;
  private Long snsPubId;
  // 是否系统根据严格查重，自动给用户确认成果
  private Integer autoComfirm = 0;
  // 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，记录自动确认次数
  private Integer syncNum = 0;

  public PublicationConfirm() {
    super();
  }

  public PublicationConfirm(Long id, Long insId) {
    super();
    this.id = id;
    this.insId = insId;
  }

  public PublicationConfirm(Long psnId, Long rolPubId, Long snsPubId) {
    super();
    this.psnId = psnId;
    this.rolPubId = rolPubId;
    this.snsPubId = snsPubId;
  }

  @Id
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "CONFIRM_DATE")
  public Date getConfirmDate() {
    return confirmDate;
  }

  @Column(name = "CONFIRM_RESULT")
  public Integer getConfirmResult() {
    return confirmResult;
  }

  @Column(name = "ASSIGN_SEQ_NO")
  public Integer getAssignSeqNo() {
    return assignSeqNo;
  }

  @Column(name = "CONFIRM_SEQ_NO")
  public Integer getConfirmSeqNo() {
    return confirmSeqNo;
  }

  @Column(name = "ROL_PUB_ID")
  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  @Column(name = "DUP_STATUS")
  public Integer getDupStatus() {
    return dupStatus;
  }

  @Column(name = "SYNC_STATUS")
  public Integer getSyncStatus() {
    return syncStatus;
  }

  public void setDupStatus(Integer dupStatus) {
    this.dupStatus = dupStatus;
  }

  @Column(name = "DUP_PUBIDS")
  public String getDupPubIds() {
    return dupPubIds;
  }

  @Column(name = "AUTO_CONFIRM")
  public Integer getAutoComfirm() {
    return autoComfirm;
  }

  @Column(name = "SYNC_NUM")
  public Integer getSyncNum() {
    return syncNum;
  }

  public void setSyncNum(Integer syncNum) {
    this.syncNum = syncNum;
  }

  public void setAutoComfirm(Integer autoComfirm) {
    this.autoComfirm = autoComfirm;
  }

  public void setDupPubIds(String dupPubIds) {
    this.dupPubIds = dupPubIds;
  }

  public void setSyncStatus(Integer syncStatus) {
    this.syncStatus = syncStatus;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setConfirmDate(Date confirmDate) {
    this.confirmDate = confirmDate;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public void setAssignSeqNo(Integer assignSeqNo) {
    this.assignSeqNo = assignSeqNo;
  }

  public void setConfirmSeqNo(Integer confirmSeqNo) {
    this.confirmSeqNo = confirmSeqNo;
  }

  @Column(name = "INS_ASSIGN_ID")
  public Long getInsAssignId() {
    return insAssignId;
  }

  public void setInsAssignId(Long insAssignId) {
    this.insAssignId = insAssignId;
  }

  @Column(name = "CONFIRM_PM_ID")
  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  @Column(name = "ASSIGN_SCORE")
  public Float getAssignScore() {
    return assignScore;
  }

  public void setAssignScore(Float assignScore) {
    this.assignScore = assignScore;
  }

  @Column(name = "SNS_PUB_ID")
  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

}
