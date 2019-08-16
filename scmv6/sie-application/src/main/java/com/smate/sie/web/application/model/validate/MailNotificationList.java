package com.smate.sie.web.application.model.validate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 邮件通知实体类
 * 
 * @author xr 2019.4.26
 */
@Entity
@Table(name = "MAIL_NOTIFICATION_LIST")
public class MailNotificationList implements Serializable {

  private static final long serialVersionUID = 1L;
  // 主键
  private Long id;
  // 名称
  private String MailKey;
  // 邮箱
  private String MailValues;

  public MailNotificationList() {
    super();
  }

  public MailNotificationList(Long id, String mailKey, String mailValues) {
    super();
    this.id = id;
    MailKey = mailKey;
    MailValues = mailValues;
  }

  public MailNotificationList(String mailKey, String mailValues) {
    super();
    MailKey = mailKey;
    MailValues = mailValues;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_MAILID", sequenceName = "SEQ_MAIL_NOTIFICATION_LIST", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MAILID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "MAIL_KEY")
  public String getMailKey() {
    return MailKey;
  }

  public void setMailKey(String mailKey) {
    MailKey = mailKey;
  }

  @Column(name = "MAIL_VALUES")
  public String getMailValues() {
    return MailValues;
  }

  public void setMailValues(String mailValues) {
    MailValues = mailValues;
  }

}
