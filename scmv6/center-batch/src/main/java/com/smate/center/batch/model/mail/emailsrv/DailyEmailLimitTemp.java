package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 每日邮件限制的模板
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "DAILY_EMAIL_LIMIT_TEMP")
public class DailyEmailLimitTemp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1772062454417013020L;
  private String tempName; // 模板名，去掉了中英文标识及扩展名
  private Integer status; // 是否启用,0，不启用,1,启用
  private String remarks; // 备注

  @Id
  @Column(name = "temp_name")
  public String getTempName() {
    return tempName;
  }

  public void setTempName(String tempName) {
    this.tempName = tempName;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "remarks")
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }


}
