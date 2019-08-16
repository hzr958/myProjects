package com.smate.center.mail.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 邮件管理通知记录表
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_MAIL_MONITOR_LOG")
public class MonitorLog {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_MAIL_MONITOR_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主建id
  @Column(name = "SENDER")
  private String sender; // 发送账号
  @Column(name = "RECEIVER")
  private String receiver;// 接收邮箱 可多个，后面 的为抄送
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 创建更新时间
  @Column(name = "MSG")
  private String msg; // 描述

  public MonitorLog() {
    super();
  }

  public MonitorLog(Long id, String sender, String receiver, Date updateDate, String msg) {
    super();
    this.id = id;
    this.sender = sender;
    this.receiver = receiver;
    this.updateDate = updateDate;
    this.msg = msg;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "MonitorLog [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", updateDate=" + updateDate
        + ", msg=" + msg + "]";
  }

}
