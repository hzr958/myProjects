package com.smate.center.batch.service.pub.mq;

import java.util.List;

/**
 * 论文推荐重新生成推荐动态的消息参数对象_SCM-5988.
 * 
 * @author mjg
 * 
 */
public class PubAllBuildReDynMessage {

  private int dynReType;// 推荐动态类型.
  private String actionType;
  @SuppressWarnings("rawtypes")
  private List reDynParamList;// 推荐动态所需参数列表.

  public PubAllBuildReDynMessage() {}

  @SuppressWarnings("rawtypes")
  public PubAllBuildReDynMessage(int dynReType, String actionType, List reDynParamList, Integer fromNodeId) {
    this.dynReType = dynReType;
    this.actionType = actionType;
    this.reDynParamList = reDynParamList;
  }

  public int getDynReType() {
    return dynReType;
  }

  public void setDynReType(int dynReType) {
    this.dynReType = dynReType;
  }

  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  @SuppressWarnings("rawtypes")
  public List getReDynParamList() {
    return reDynParamList;
  }

  @SuppressWarnings("rawtypes")
  public void setReDynParamList(List reDynParamList) {
    this.reDynParamList = reDynParamList;
  }

}
