package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 系统邮件原始数据备份表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "mail_init_data_his")
public class MailInitDataHis implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6133651748423807512L;
  private Long id;
  private String mailData;
  private Integer fromNodeId;
  private Date createDate;
  private Integer status;
  private String toAddress;

  @Id
  @Column(name = "id")
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
