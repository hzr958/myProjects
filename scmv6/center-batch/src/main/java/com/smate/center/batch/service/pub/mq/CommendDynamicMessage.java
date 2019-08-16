package com.smate.center.batch.service.pub.mq;

import net.sf.json.JSONObject;

/**
 * 生成动态共用消息实体2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
public class CommendDynamicMessage {


  private JSONObject jsonObject;
  private String dynJson;
  private boolean extFlag;

  public CommendDynamicMessage() {
    super();
  }

  public CommendDynamicMessage(JSONObject jsonObject, String dynJson, boolean extFlag) {
    super();
    this.jsonObject = jsonObject;
    this.dynJson = dynJson;
    this.extFlag = extFlag;
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public void setJsonObject(JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public String getDynJson() {
    return dynJson;
  }

  public void setDynJson(String dynJson) {
    this.dynJson = dynJson;
  }

  public boolean getExtFlag() {
    return extFlag;
  }

  public void setExtFlag(boolean extFlag) {
    this.extFlag = extFlag;
  }

}
