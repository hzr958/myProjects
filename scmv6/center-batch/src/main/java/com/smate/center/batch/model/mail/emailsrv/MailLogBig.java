package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 邮件日志大字段表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "MAIL_LOG_BIG")
public class MailLogBig implements Serializable {

  private static final long serialVersionUID = 6876705732560308559L;

  private Long id;
  private Long mailId;
  // 邮件参数，JSON格式
  private String context;

  public MailLogBig() {
    super();
  }

  public MailLogBig(Long mailId, String context) {
    super();
    this.mailId = mailId;
    this.context = context;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MAIL_LOG_BIG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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
