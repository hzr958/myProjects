package com.smate.core.base.utils.model.msg;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 短信记录表
 * 
 * @author LJ
 *
 *         2017年6月30日
 */
@Entity
@Table(name = "MESSAGE_LOG")
public class MessageLog implements Serializable {

  private static final long serialVersionUID = -234622919902773393L;

  // 短信日志表
  @Id
  @Column(name = "LOG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MESSAGE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long logId;

  // sms_type短信类型
  @Column(name = "SMS_TYPE")
  private Long smsType;

  // 短信接收人，多个用逗号分隔
  @Column(name = "SMS_TO")
  private String smsTo;

  // 短信内容
  @Column(name = "CONTENT")
  private String content;

  // 发送时间
  @Column(name = "SEND_TIME")
  private Date sendTime;

  // 发送状态，1发送成功，2发送失败
  @Column(name = "STATUS")
  private Integer status;

  // 发送状态，1发送成功，2发送失败
  @Column(name = "ERROR_MSG")
  private String errormsg;

  @Column(name = "PRODUCE_LOG_PSN_ID")
  private Long produceLogPsnId;

  public Long getProduceLogPsnId() {
    return produceLogPsnId;
  }

  public void setProduceLogPsnId(Long produceLogPsnId) {
    this.produceLogPsnId = produceLogPsnId;
  }

  public Long getLogId() {
    return logId;
  }

  public void setLogId(Long logId) {
    this.logId = logId;
  }

  public Long getSmsType() {
    return smsType;
  }

  public void setSmsType(Long smsType) {
    this.smsType = smsType;
  }

  public String getSmsTo() {
    return smsTo;
  }

  public void setSmsTo(String smsTo) {
    this.smsTo = smsTo;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getErrormsg() {
    return errormsg;
  }

  public void setErrormsg(String errormsg) {
    this.errormsg = errormsg;
  }

  public MessageLog(Long logId, Long smsType, String smsTo, String content, Date sendTime, Integer status,
      String errormsg) {
    super();
    this.logId = logId;
    this.smsType = smsType;
    this.smsTo = smsTo;
    this.content = content;
    this.sendTime = sendTime;
    this.status = status;
    this.errormsg = errormsg;
  }

  public MessageLog(Long smsType, String smsTo, String content, Date sendTime, Integer status, String errormsg,
      Long produceLogPsnId) {
    this.smsType = smsType;
    this.smsTo = smsTo;
    this.content = content;
    this.sendTime = sendTime;
    this.status = status;
    this.errormsg = errormsg;
    this.produceLogPsnId = produceLogPsnId;
  }

  public MessageLog() {
    super();
  }


}
