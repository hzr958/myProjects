package com.smate.center.batch.model.rcmd.pub;

import java.util.Map;

import com.smate.center.batch.constant.PubConfirmSyncMessageEnum;


/**
 * 成果确认，成果同步回单位.
 * 
 * @author LY
 * 
 */
public class PubConfirmSyncMessage {


  private Integer nodeId;
  private Long insId;
  private Long insPubId;
  private Long snsPubId;
  private Long assignId;
  // 引用情况
  private Map<String, String> pubList;
  // 发表日期
  private String publishDate;
  private String pubXml;
  private Long psnId;
  // 确认状态0不是我的成果，1是我的成果
  private Integer confirmResult;
  // pub_memberid
  private Long pmId;
  private PubConfirmSyncMessageEnum actionType;

  public PubConfirmSyncMessageEnum getActionType() {
    return actionType;
  }

  public void setActionType(PubConfirmSyncMessageEnum actionType) {
    this.actionType = actionType;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getInsPubId() {
    return insPubId;
  }

  public Long getAssignId() {
    return assignId;
  }

  public String getPubXml() {
    return pubXml;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsPubId(Long insPubId) {
    this.insPubId = insPubId;
  }

  public void setAssignId(Long assignId) {
    this.assignId = assignId;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public Map<String, String> getPubList() {
    return pubList;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPubList(Map<String, String> pubList) {
    this.pubList = pubList;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

}
