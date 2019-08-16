package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果确认实体历史备份.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PUB_CONFIRM_HI")
public class PublicationConfirmHi implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2068952622186741619L;
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

  public PublicationConfirmHi() {
    super();
  }

  public PublicationConfirmHi(Long id, Long psnId, Long insId, Date confirmDate, Integer confirmResult,
      Integer assignSeqNo, Integer confirmSeqNo, Long rolPubId, Long insAssignId, Long pmId, Integer dupStatus,
      Integer syncStatus, String dupPubIds, Float assignScore, Long snsPubId, Integer autoComfirm) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.insId = insId;
    this.confirmDate = confirmDate;
    this.confirmResult = confirmResult;
    this.assignSeqNo = assignSeqNo;
    this.confirmSeqNo = confirmSeqNo;
    this.rolPubId = rolPubId;
    this.insAssignId = insAssignId;
    this.pmId = pmId;
    this.dupStatus = dupStatus;
    this.syncStatus = syncStatus;
    this.dupPubIds = dupPubIds;
    this.assignScore = assignScore;
    this.snsPubId = snsPubId;
    this.autoComfirm = autoComfirm;
  }

  public PublicationConfirmHi(PublicationConfirm pc) {

    this.id = pc.getId();
    this.psnId = pc.getPsnId();
    this.insId = pc.getInsId();
    this.confirmDate = pc.getConfirmDate();
    this.confirmResult = pc.getConfirmResult();
    this.assignSeqNo = pc.getAssignSeqNo();
    this.confirmSeqNo = pc.getConfirmSeqNo();
    this.rolPubId = pc.getRolPubId();
    this.insAssignId = pc.getInsAssignId();
    this.pmId = pc.getPmId();
    this.dupStatus = pc.getDupStatus();
    this.syncStatus = pc.getSyncStatus();
    this.dupPubIds = pc.getDupPubIds();
    this.assignScore = pc.getAssignScore();
    this.snsPubId = pc.getSnsPubId();
    this.autoComfirm = pc.getAutoComfirm();
  }

  @Id
  @Column(name = "ID")
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
