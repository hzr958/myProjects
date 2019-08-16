package com.smate.core.base.email.model;

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
 * 
 * 系统邮件原始数据表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "MAIL_INIT_DATA")
public class MailInitData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8573678024374242187L;
  private Long id;
  private String mailData;
  private Integer fromNodeId;
  private Date createDate;
  private Integer status;
  private String toAddress;

  public MailInitData() {}

  public MailInitData(String mailData, Integer fromNodeId) {
    this.mailData = mailData;
    this.fromNodeId = fromNodeId;
    this.createDate = new Date();
    this.status = 0;
  }

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MAIL_INIT_DATA", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "mail_data")
  public String getMailData() {
    return mailData;
  }

  @Column(name = "from_node_id")
  public Integer getFromNodeId() {
    return fromNodeId;
  }

  @Column(name = "create_date")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "TO_ADDRESS")
  public String getToAddress() {
    return toAddress;
  }

  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setMailData(String mailData) {
    this.mailData = mailData;
  }

  public void setFromNodeId(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
