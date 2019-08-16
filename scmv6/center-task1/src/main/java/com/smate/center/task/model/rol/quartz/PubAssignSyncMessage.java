package com.smate.center.task.model.rol.quartz;

import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.rcmd.quartz.PubDupFields;

/**
 * 成果指派MQ消息，包括成果XML.
 * 
 * @author zjh
 *
 */
public class PubAssignSyncMessage {
  private static final long serialVersionUID = 7394034313112443190L;

  private Long psnId;
  private Long insId;
  private Long assignId;
  private Long insPubId;
  private Long pmId;
  private Integer seqNo;
  private String pubXml;
  // 同步成果XML失败类型，1单位删除了指派关系、2成果人员被删除
  private Integer errorType;
  private PubConfirmRolPub pubConfirmRolPub;
  private PubDupFields pubDupFields;
  private PubAssignSyncMessageEnum actionType;
  private Float assignScore;
  private int isSaveRcmdPubConfirm;// 是否要重复保存rcmdPubConfirm

  public PubAssignSyncMessage() {
    super();
  }

  public PubAssignSyncMessage(Long psnId, Long insId, Long assignId, Long insPubId, PubConfirmRolPub pubConfirmRolPub,
      PubAssignSyncMessageEnum actionType, Integer seqNo, Integer fromNodeId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.assignId = assignId;
    this.insPubId = insPubId;
    this.pubConfirmRolPub = pubConfirmRolPub;
    this.actionType = actionType;
    this.seqNo = seqNo;
  }

  public PubAssignSyncMessage(Long psnId, Long insId, Long assignId, Long insPubId, PubAssignSyncMessageEnum actionType,
      Integer fromNodeId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.assignId = assignId;
    this.insPubId = insPubId;
    this.actionType = actionType;
  }

  public PubAssignSyncMessage(Long psnId, Long insId, Long insPubId, String pubXml, PubAssignSyncMessageEnum actionType,
      Integer fromNodeId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.insPubId = insPubId;
    this.pubXml = pubXml;
    this.actionType = actionType;
  }

  public PubAssignSyncMessage(Long psnId, Long insId, Long insPubId, PubAssignSyncMessageEnum actionType,
      Integer errorType, Integer fromNodeId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.insPubId = insPubId;
    this.errorType = errorType;
    this.actionType = actionType;
  }

  public PubAssignSyncMessage(Long psnId, Long insId, Long assignId, Long insPubId, PubConfirmRolPub pubConfirmRolPub,
      PubAssignSyncMessageEnum actionType, Long pmId, Integer seqNo, Integer fromNodeId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.assignId = assignId;
    this.insPubId = insPubId;
    this.pubConfirmRolPub = pubConfirmRolPub;
    this.actionType = actionType;
    this.pmId = pmId;
    this.seqNo = seqNo;
  }

  public Float getAssignScore() {
    return assignScore;
  }

  public void setAssignScore(Float assignScore) {
    this.assignScore = assignScore;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public PubConfirmRolPub getPubConfirmRolPub() {
    return pubConfirmRolPub;
  }

  public void setPubConfirmRolPub(PubConfirmRolPub pubConfirmRolPub) {
    this.pubConfirmRolPub = pubConfirmRolPub;
  }

  public Long getAssignId() {
    return assignId;
  }

  public Long getInsPubId() {
    return insPubId;
  }

  public String getPubXml() {
    return pubXml;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getErrorType() {
    return errorType;
  }

  public void setErrorType(Integer errorType) {
    this.errorType = errorType;
  }

  public void setAssignId(Long assignId) {
    this.assignId = assignId;
  }

  public void setInsPubId(Long insPubId) {
    this.insPubId = insPubId;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

  public PubAssignSyncMessageEnum getActionType() {
    return actionType;
  }

  public void setActionType(PubAssignSyncMessageEnum actionType) {
    this.actionType = actionType;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public PubDupFields getPubDupFields() {
    return pubDupFields;
  }

  public void setPubDupFields(PubDupFields pubDupFields) {
    this.pubDupFields = pubDupFields;
  }

  public int getIsSaveRcmdPubConfirm() {
    return isSaveRcmdPubConfirm;
  }

  public void setIsSaveRcmdPubConfirm(int isSaveRcmdPubConfirm) {
    this.isSaveRcmdPubConfirm = isSaveRcmdPubConfirm;
  }


}
