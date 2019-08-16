package com.smate.center.task.model.snsbak;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NEW_YEAR_GREETING_EMAIL")
public class NewYearGreetingEmail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6600912082630190009L;
  private Long mailId;
  private String email;
  private Integer status;

  public NewYearGreetingEmail() {
    super();
  }

  @Id
  @Column(name = "MAIL_ID")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
