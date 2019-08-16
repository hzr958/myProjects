package com.smate.center.batch.service.pub.mq;

/**
 * 
 * 同步邮件至邮件服务
 * 
 * @author zk
 * 
 */
public class SyncMailToMailSrvMessage {

  // 邮件类型，定时邮件，立即发送邮件等
  private Integer mailType;
  // 邮件标识，整理邮件时是否出错（出错则做日志记录，不发邮件）
  private String mailFlag;
  private String mailJson;

  public Integer getMailType() {
    return mailType;
  }

  public String getMailFlag() {
    return mailFlag;
  }

  public String getMailJson() {
    return mailJson;
  }

  public void setMailType(Integer mailType) {
    this.mailType = mailType;
  }

  public void setMailFlag(String mailFlag) {
    this.mailFlag = mailFlag;
  }

  public void setMailJson(String mailJson) {
    this.mailJson = mailJson;
  }

}
