package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "v_send_mail_psn_log")
public class SendmailPsnLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 977705709217198763L;
  private Long id;
  private Long psnId;
  private Integer sendStatus;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SEND_MAIL_PSN_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SEND_STATUS")
  public Integer getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }



}
