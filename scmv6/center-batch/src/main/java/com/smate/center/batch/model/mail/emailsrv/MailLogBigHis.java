package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邮件数据备份表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "MAIL_LOG_BIG_HIS")
public class MailLogBigHis implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3049966269571840689L;
  private Long id;
  private Long mailId;
  // 邮件参数，JSON格式
  private String context;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "MAIL_ID")
  public Long getMailId() {
    return mailId;
  }

  @Column(name = "CONTEXT")
  public String getContext() {
    return context;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public void setContext(String context) {
    this.context = context;
  }

}
