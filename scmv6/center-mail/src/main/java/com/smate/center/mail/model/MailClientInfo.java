package com.smate.center.mail.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 邮件发送客户端 信息类
 * 
 * @author tsz
 *
 */
public class MailClientInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8588770540125401177L;

  private String clientName;

  private Date lastUpdateTime;

  private String clientIp;

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

}
