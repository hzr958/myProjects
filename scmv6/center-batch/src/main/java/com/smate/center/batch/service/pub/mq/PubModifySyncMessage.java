package com.smate.center.batch.service.pub.mq;

import com.smate.center.batch.model.sns.pub.Publication;

/**
 * 成果、文献更新发送刷新记录消息体.
 * 
 * @author WeiLong Peng
 * 
 */
public class PubModifySyncMessage {


  private Publication pub;
  private Integer isDel = 0;

  public PubModifySyncMessage() {}

  public PubModifySyncMessage(Publication pub, Integer isDel, Integer fromNodeId) {
    this.pub = pub;
    this.isDel = isDel;
  }

  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public Publication getPub() {
    return pub;
  }

  public void setPub(Publication pub) {
    this.pub = pub;
  }

}
