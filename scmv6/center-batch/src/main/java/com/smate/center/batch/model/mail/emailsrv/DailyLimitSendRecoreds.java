package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 每日限制发送日志(记录限制模板每日发送记录)
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "DAILY_LIMIT_SEND_RECOREDS")
public class DailyLimitSendRecoreds implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8302684211278105118L;
  private DailyLimitSendRecoredsPk pk; // 主键
  private Date sendDate; // 发送时间

  @EmbeddedId
  public DailyLimitSendRecoredsPk getPk() {
    return pk;
  }

  public void setPk(DailyLimitSendRecoredsPk pk) {
    this.pk = pk;
  }

  @Column(name = "SEND_DATE")
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }



}
