package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;
import java.util.UUID;

/**
 * 本地消息父类.
 * 
 * @author liqinghua
 * 
 */
public class BaseLocalQueneMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8240534827750232949L;
  private String msguuid;
  private Long insId;
  // 请求人
  private Long opPsnId;
  private Long msgId;
  private Integer nodeId;
  // 消息处理优先级，最低1，最高9，默认9
  private Integer priority = 9;

  public BaseLocalQueneMessage() {
    super();
    initMsg();
  }

  public BaseLocalQueneMessage(Integer nodeId) {
    super();
    this.nodeId = nodeId;
    initMsg();
  }

  public BaseLocalQueneMessage(Integer nodeId, Integer priority) {
    super();
    this.nodeId = nodeId;
    this.priority = priority;
  }

  public BaseLocalQueneMessage(Long opPsnId) {
    super();
    this.opPsnId = opPsnId;
    initMsg();
  }

  private void initMsg() {
    this.msguuid = UUID.randomUUID().toString().replace("-", "");
  }

  public String getMsguuid() {
    return msguuid;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getOpPsnId() {
    return opPsnId;
  }

  public Long getMsgId() {
    return msgId;
  }

  public void setMsguuid(String msguuid) {
    this.msguuid = msguuid;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setOpPsnId(Long opPsnId) {
    this.opPsnId = opPsnId;
  }

  public void setMsgId(Long msgId) {
    this.msgId = msgId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

}
