package com.smate.center.batch.service.pub.mq;


/**
 * 资源删除同步消息
 * 
 * @author Scy
 * 
 */
public class DelResMessage {

  private Long actionKey;
  private Integer actionType;
  private Long psnId;

  public DelResMessage(Long actionKey, Integer actionType, Long psnId, Integer fromNodeId) {
    super();
    this.actionKey = actionKey;
    this.actionType = actionType;
    this.psnId = psnId;
  }

  public Long getActionKey() {
    return actionKey;
  }

  public void setActionKey(Long actionKey) {
    this.actionKey = actionKey;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
