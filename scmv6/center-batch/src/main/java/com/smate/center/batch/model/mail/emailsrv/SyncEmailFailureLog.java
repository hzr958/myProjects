package com.smate.center.batch.model.mail.emailsrv;

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
 * 邮件生成出错记录表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "SYNC_EMAIL_FAILURE_LOG")
public class SyncEmailFailureLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8319610704840749136L;
  private Long id;
  private Integer fromNode;
  private String emailJson;
  private Date ctDate;
  private String errorMsg;
  private String toAddress;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYNC_EMAIL_FAILURE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "from_node")
  public Integer getFromNode() {
    return fromNode;
  }

  @Column(name = "emailjson")
  public String getEmailJson() {
    return emailJson;
  }

  @Column(name = "ct_date")
  public Date getCtDate() {
    return ctDate;
  }

  @Column(name = "TO_ADDRESS")
  public String getToAddress() {
    return toAddress;
  }

  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFromNode(Integer fromNode) {
    this.fromNode = fromNode;
  }

  public void setEmailJson(String emailJson) {
    this.emailJson = emailJson;
  }

  public void setCtDate(Date ctDate) {
    this.ctDate = ctDate;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

}
