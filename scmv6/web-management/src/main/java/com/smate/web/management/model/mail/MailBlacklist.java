package com.smate.web.management.model.mail;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 邮件黑名单表
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_MAIL_BLACKLIST")
public class MailBlacklist {

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_MAIL_BLACKLIST", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主建id
  @Column(name = "EMAIL")
  private String email; // 拉黑的邮箱
  @Column(name = "STATUS")
  private int status;// 状态 0开启 1关闭
  @Column(name = "TYPE")
  private int type; // 0 邮箱类 1 域名类
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 创建更新时间
  @Column(name = "MSG")
  private String msg; // 描述

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

  public MailBlacklist(Long id, String email, int status, Date updateDate, String msg) {
    super();
    this.id = id;
    this.email = email;
    this.status = status;
    this.updateDate = updateDate;
    this.msg = msg;
  }

  public MailBlacklist() {
    super();
  }

  @Override
  public String toString() {
    return "MailBlacklist [id=" + id + ", email=" + email + ", status=" + status + ", updateDate=" + updateDate
        + ", msg=" + msg + "]";
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

}
