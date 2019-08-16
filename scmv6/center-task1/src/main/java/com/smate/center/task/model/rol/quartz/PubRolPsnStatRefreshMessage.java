package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;
import java.util.List;

public class PubRolPsnStatRefreshMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 477237765139318782L;

  private Long insId;
  private Long psnId;
  private Long pubId;
  private List<Long> psnIds;
  private Integer actionType;

  public PubRolPsnStatRefreshMessage() {

  }

  public PubRolPsnStatRefreshMessage(Long insId, Long psnId, Integer actionType, Integer nodeId) {
    super();
    this.insId = insId;
    this.psnId = psnId;
    this.actionType = actionType;

  }

  public PubRolPsnStatRefreshMessage(Long insId, Integer actionType, Long pubId, Integer nodeId) {
    super();
    this.insId = insId;
    this.pubId = pubId;
    this.actionType = actionType;
  }

  public PubRolPsnStatRefreshMessage(Long insId, List<Long> psnIds, Integer actionType, Integer nodeId) {
    super();
    this.insId = insId;
    this.psnIds = psnIds;
    this.actionType = actionType;

  }

  public PubRolPsnStatRefreshMessage(Long insId, Integer actionType, Integer nodeId) {
    super();
    this.insId = insId;
    this.actionType = actionType;

  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public List<Long> getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(List<Long> psnIds) {
    this.psnIds = psnIds;
  }

}
