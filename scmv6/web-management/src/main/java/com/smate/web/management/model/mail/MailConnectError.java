package com.smate.web.management.model.mail;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 链接收件箱错误记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MAIL_CONNECT_ERROR")
public class MailConnectError implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_MAIL_CONNECT_ERROR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "ACCOUNT")
  private String account;
  @Column(name = "PWD")
  private String pwd;
  @Column(name = "HOST")
  private String host;
  @Column(name = "MSG")
  private String msg;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    if (msg.length() > 2000) {
      msg = msg.substring(0, 2000);
    }
    this.msg = msg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }



}
