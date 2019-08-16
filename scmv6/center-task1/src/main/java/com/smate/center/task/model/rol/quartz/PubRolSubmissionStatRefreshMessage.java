package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
public class PubRolSubmissionStatRefreshMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4939655990477330752L;
  private Long insId;
  private Long psnId;
  private Integer actionType;

  public PubRolSubmissionStatRefreshMessage() {

  }

  public PubRolSubmissionStatRefreshMessage(Long insId, Long psnId, Integer actionType, Integer nodeId) {
    super();
    this.insId = insId;
    this.psnId = psnId;
    this.actionType = actionType;

  }

  public PubRolSubmissionStatRefreshMessage(Long insId, Integer actionType, Integer nodeId) {
    super();
    this.insId = insId;
    this.actionType = actionType;

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

}
