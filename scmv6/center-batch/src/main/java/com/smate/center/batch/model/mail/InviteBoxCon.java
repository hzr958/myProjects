package com.smate.center.batch.model.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 站内请求关联邮件内容表.
 * 
 * @see 表中字段inviteId的值与邀请收件箱表invite_inbox表中的主键ID相同.
 * 
 * @author maojianguo
 * 
 */
@SuppressWarnings("javadoc")
@Entity
@Table(name = "INVITE_BOX_CON")
public class InviteBoxCon implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 2057366275835484907L;
  private Long inviteId;
  private String content;
  private String mailContent;

  public InviteBoxCon() {
    super();
  }

  public InviteBoxCon(Long inviteId, String content, String mailContent) {
    super();
    this.inviteId = inviteId;
    this.content = content;
    this.mailContent = mailContent;
  }

  @Id
  @Column(name = "INVITE_ID")
  public Long getInviteId() {
    return inviteId;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  @Column(name = "MAIL_CONTENT")
  public String getMailContent() {
    return mailContent;
  }

  public void setInviteId(Long inviteId) {
    this.inviteId = inviteId;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setMailContent(String mailContent) {
    this.mailContent = mailContent;
  }
}
