package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 邮件推广状态类
 * 
 * @author zk
 * 
 */

@Entity
@Table(name = "MAIL_PROMOTE_STATUS")
public class MailPromoteStatus implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7745015756147249474L;
  // 主键id
  private Long id;
  // 生成邮件状态
  private Integer startStatus;
  // 发送邮件状态
  private Integer sendStatus;
  // 中文模板id
  private Integer tempIdZh;
  // 英文模板id
  private Integer tempIdEn;
  // 更新时间
  private Date updateDate;

  public MailPromoteStatus() {}

  public MailPromoteStatus(Long id, Integer startStatus, Integer sendStatus, Integer tempIdZh, Integer tempIdEn,
      Date updateDate) {
    this.id = id;
    this.startStatus = startStatus;
    this.sendStatus = sendStatus;
    this.tempIdZh = tempIdZh;
    this.tempIdEn = tempIdEn;
    this.updateDate = updateDate;
  }

  @Id
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  @Column(name = "start_status")
  public Integer getStartStatus() {
    return startStatus;
  }

  @Column(name = "send_status")
  public Integer getSendStatus() {
    return sendStatus;
  }

  @Column(name = "temp_code_zh")
  public Integer getTempIdZh() {
    return tempIdZh;
  }

  @Column(name = "temp_code_en")
  public Integer getTempIdEn() {
    return tempIdEn;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setTempIdZh(Integer tempIdZh) {
    this.tempIdZh = tempIdZh;
  }

  public void setTempIdEn(Integer tempIdEn) {
    this.tempIdEn = tempIdEn;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setStartStatus(Integer startStatus) {
    this.startStatus = startStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }

}
