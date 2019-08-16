package com.smate.center.batch.service.pub.mq;

import java.util.Map;

/**
 * 收件箱-站内邀请参数实体类.
 * 
 * @author maojianguo
 * 
 */
public class SnsInviteMailMessage {


  private Map<String, Object> param;// 请求参数信息.
  private Integer messageType;// 邀请类型(对应DynamicConstant类中RES参数值).

  public SnsInviteMailMessage(Integer fromNodeId, Map<String, Object> param, Integer messageType) {
    this.param = param;
    this.messageType = messageType;
  }

  public Map<String, Object> getParam() {
    return param;
  }

  public Integer getMessageType() {
    return messageType;
  }

  public void setParam(Map<String, Object> param) {
    this.param = param;
  }

  public void setMessageType(Integer messageType) {
    this.messageType = messageType;
  }

}
