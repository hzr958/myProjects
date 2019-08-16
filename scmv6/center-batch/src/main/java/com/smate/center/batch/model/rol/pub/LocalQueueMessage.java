package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 本地MQ消息存储实体类.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "LOCAL_QUEUE_MESSAGE")
public class LocalQueueMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1410848063566565318L;

  private Long id;
  // 消费者bean ID
  private String beanName;
  // 消息体
  private String msgBody;
  // 创建时间
  private Date createAt;
  // 0/1: 0等待处理,2正在处理，9处理失败,
  private Integer state;
  // 消息实体类名
  private String msgClz;
  // 消息处理优先级，最低1，最高9，默认9
  private Integer priority = 9;
  // 错误次数，可以尝试3次
  private Integer errorNum = 0;
  // 创建时间
  private Date opAt;

  public LocalQueueMessage() {
    super();
  }

  public LocalQueueMessage(Long id, String beanName, String msgBody, String msgClz) {
    super();
    this.id = id;
    this.beanName = beanName;
    this.msgBody = msgBody;
    this.msgClz = msgClz;
    this.state = 0;
    this.priority = 9;
    this.errorNum = 0;
    this.createAt = new Date();
    this.opAt = new Date();
  }

  public LocalQueueMessage(Long id, String beanName, String msgBody, String msgClz, Integer priority) {
    super();
    this.id = id;
    this.beanName = beanName;
    this.msgBody = msgBody;
    this.msgClz = msgClz;
    this.state = 0;
    this.priority = priority;
    this.errorNum = 0;
    this.createAt = new Date();
    this.opAt = new Date();
  }

  @Id
  @Column(name = "MSG_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "BEAN_NAME")
  public String getBeanName() {
    return beanName;
  }

  @Column(name = "MSG_BODY")
  public String getMsgBody() {
    return msgBody;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  @Column(name = "STATE")
  public Integer getState() {
    return state;
  }

  @Column(name = "MSG_CLZ")
  public String getMsgClz() {
    return msgClz;
  }

  @Column(name = "PRIORITY")
  public Integer getPriority() {
    return priority;
  }

  @Column(name = "ERROR_NUM")
  public Integer getErrorNum() {
    return errorNum;
  }

  @Column(name = "OP_AT")
  public Date getOpAt() {
    return opAt;
  }

  public void setOpAt(Date opAt) {
    this.opAt = opAt;
  }

  public void setErrorNum(Integer errorNum) {
    this.errorNum = errorNum;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public void setMsgBody(String msgBody) {
    this.msgBody = msgBody;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public void setMsgClz(String msgClz) {
    this.msgClz = msgClz;
  }

}
