package com.smate.web.psn.model.statistics;

/**
 * 
 * 他人访问的记录
 * 
 * @author zx
 *
 */
public class StatisticsForm {

  private Long vistPsnId;// 被访问的主页的psnID
  private String vistPsnDes3Id;// 被访问的主页的加密的psnID
  private Long actionKey; // 访问主页的话就是psnId,成果的话就是pubId,项目的话就是项目Id
  private String actionDes3Key;// 加密的actionKey
  private Integer actionType;// 访问他人页面的类型 具体看DynamicConstant.java

  public Long getVistPsnId() {
    return vistPsnId;
  }

  public void setVistPsnId(Long vistPsnId) {
    this.vistPsnId = vistPsnId;
  }

  public String getVistPsnDes3Id() {
    return vistPsnDes3Id;
  }

  public void setVistPsnDes3Id(String vistPsnDes3Id) {
    this.vistPsnDes3Id = vistPsnDes3Id;
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

  public String getActionDes3Key() {
    return actionDes3Key;
  }

  public void setActionDes3Key(String actionDes3Key) {
    this.actionDes3Key = actionDes3Key;
  }

}
