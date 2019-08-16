package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 本地MQ消息处理错误存储实体类.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "LOCAL_QUEUE_ERROR")
public class LocalQueueError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1651911273753448028L;

  // 消息ID
  private Long msgId;
  // 消息处理错误内容
  private String errorMsg;

  public LocalQueueError() {
    super();
  }

  public LocalQueueError(Long msgId, String errorMsg) {
    super();
    this.msgId = msgId;
    this.errorMsg = errorMsg;
  }

  @Id
  @Column(name = "MSG_ID")
  public Long getMsgId() {
    return msgId;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setMsgId(Long msgId) {
    this.msgId = msgId;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

}
