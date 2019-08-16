package com.smate.web.management.model.mail;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

public class MailDetailsForm {
  private String des3MailId;
  private Long mailId;

  public String getDes3MailId() {
    if (mailId != null && StringUtils.isBlank(des3MailId)) {
      des3MailId = Des3Utils.encodeToDes3(mailId.toString());
    }
    return des3MailId;
  }

  public void setDes3MailId(String des3MailId) {
    this.des3MailId = des3MailId;
  }

  public Long getMailId() {
    if (mailId == null && StringUtils.isNotBlank(des3MailId)) {
      mailId = Long.parseLong(Des3Utils.decodeFromDes3(des3MailId));
    }
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }
}
