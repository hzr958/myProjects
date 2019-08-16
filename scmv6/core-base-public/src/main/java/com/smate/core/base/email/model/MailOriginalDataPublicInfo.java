package com.smate.core.base.email.model;

import java.io.Serializable;

/**
 * 发送邮件json数据封装类
 * 
 * @author Administrator
 *
 */
public class MailOriginalDataPublicInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long senderPsnId = 0L;// 发送者psnId ，0=系统邮件
  private Long receiverPsnId = 0L;// 接收者id ,0=非科研之友用户
  private String receiver;// 接收邮箱
  private Integer mailTemplateCode; // 模版编号
  private String priorLevel;// 优先级别
  private String msg = "";// 描述

  public Long getSenderPsnId() {
    return senderPsnId;
  }

  public void setSenderPsnId(Long senderPsnId) {
    this.senderPsnId = senderPsnId;
  }

  public Long getReceiverPsnId() {
    return receiverPsnId;
  }

  public void setReceiverPsnId(Long receiverPsnId) {
    this.receiverPsnId = receiverPsnId;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public Integer getMailTemplateCode() {
    return mailTemplateCode;
  }

  public void setMailTemplateCode(Integer mailTemplateCode) {
    this.mailTemplateCode = mailTemplateCode;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
