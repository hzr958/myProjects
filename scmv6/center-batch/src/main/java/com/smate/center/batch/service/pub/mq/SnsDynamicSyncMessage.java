package com.smate.center.batch.service.pub.mq;

import com.smate.center.batch.model.sns.pub.Dynamic;

/**
 * 动态同步message.
 * 
 * @author chenxiangrong
 * 
 */
public class SnsDynamicSyncMessage {

  /**
   * 
   */
  private Dynamic dynamic;
  private String extJson;
  private int resType;

  public SnsDynamicSyncMessage(int fromNode, Dynamic dynamic, String extJson, int resType) {
    super();
    this.dynamic = dynamic;
    this.extJson = extJson;
    this.resType = resType;
  }

  public Dynamic getDynamic() {
    return dynamic;
  }

  public void setDynamic(Dynamic dynamic) {
    this.dynamic = dynamic;
  }

  public String getExtJson() {
    return extJson;
  }

  public void setExtJson(String extJson) {
    this.extJson = extJson;
  }

  public int getResType() {
    return resType;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

}
