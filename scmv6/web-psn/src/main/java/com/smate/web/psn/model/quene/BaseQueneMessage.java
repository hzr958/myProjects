package com.smate.web.psn.model.quene;



import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 队列消息基类.
 * 
 */
public abstract class BaseQueneMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1894760703747221753L;

  // 来源节点id
  private Integer fromNodeId;
  // 去向节点
  private Integer toNodeId;
  // 产生消息时间.
  private Date createAt;
  private String msguuid;
  private Long insId;
  // 请求人
  private Long opPsnId;
  private Long msgId;

  public BaseQueneMessage() {
    super();
  }

  public BaseQueneMessage(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
    this.createAt = new Date();
    this.msguuid = UUID.randomUUID().toString().replace("-", "");
  }

  public BaseQueneMessage(Integer fromNodeId, Long insId) {
    this.fromNodeId = fromNodeId;
    this.createAt = new Date();
    this.insId = insId;
    this.msguuid = UUID.randomUUID().toString().replace("-", "");
  }

  public BaseQueneMessage(Integer fromNodeId, Integer toNodeId) {
    this.fromNodeId = fromNodeId;
    this.createAt = new Date();
    this.toNodeId = toNodeId;
    this.msguuid = UUID.randomUUID().toString().replace("-", "");
  }

  public BaseQueneMessage(Integer fromNodeId, Integer toNodeId, Long opPsnId) {
    super();
    this.fromNodeId = fromNodeId;
    this.toNodeId = toNodeId;
    this.opPsnId = opPsnId;
  }

  public BaseQueneMessage(Long opPsnId) {
    super();
    this.opPsnId = opPsnId;
  }

  public Integer getFromNodeId() {
    return fromNodeId;
  }

  public void setFromNodeId(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  /**
   * @return the msguuid
   */
  public String getMsguuid() {
    return msguuid;
  }

  public void setMsguuid(String msguuid) {
    this.msguuid = msguuid;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getToNodeId() {
    return toNodeId;
  }

  public void setToNodeId(Integer toNodeId) {
    this.toNodeId = toNodeId;
  }

  public Long getOpPsnId() {
    return opPsnId;
  }

  public Long getMsgId() {
    return msgId;
  }

  public void setOpPsnId(Long opPsnId) {
    this.opPsnId = opPsnId;
  }

  public void setMsgId(Long msgId) {
    this.msgId = msgId;
  }

}
