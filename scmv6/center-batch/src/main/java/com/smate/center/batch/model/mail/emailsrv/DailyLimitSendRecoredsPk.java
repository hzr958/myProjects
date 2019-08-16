package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 每日限制发送日志(记录限制模板每日发送记录) 主键
 * 
 * @author zk
 *
 */
@Embeddable
public class DailyLimitSendRecoredsPk implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -6382986912600575885L;
  private String email; // 收件箱
  private String tempName; // 模板名

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "TEMP_NAME")
  public String getTempName() {
    return tempName;
  }

  public void setTempName(String tempName) {
    this.tempName = tempName;
  }


}
