package com.smate.web.management.model.mail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "V_MAIL_WHITELIST")
public class MailWhitelist {
  @Id
  @Column(name = "ID")
  private Long id; // id
  @Column(name = "EMAIL")
  private String email;// 邮箱
  @Column(name = "STATUS")
  private int status; // 状态0 开启1 关闭

  public MailWhitelist(Long id, String email, int status) {
    super();
    this.id = id;
    this.email = email;
    this.status = status;
  }

  public MailWhitelist() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "MailWhitelist [id=" + id + ", email=" + email + ", status=" + status + "]";
  }

}
