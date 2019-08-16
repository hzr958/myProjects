package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 全文请求发件箱.
 * 
 * @author cxr
 * 
 */
@Entity
@Table(name = "FULLTEXT_MAILBOX")
public class FullTextMailBox implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5723576641458229025L;
  private Long mailId;// ID
  private Long senderId;// 发件人ID
  private String senderName;// 发件人姓名
  private String senderEnName;// 发件人英文名
  private String senderTitLogo;// 发件人头衔
  private String senderAvatar;// 发件人头像
  private String mailTitle;// 发件箱标题
  private String mailEnTitle;// 发件箱英文标题
  private String paramJson;// 参数
  private Date sendDate;// 发送时间
  private int status;// 状态：0：正常；1：删除
  private Map<String, String> paramMap;
  private String des3MailId;

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FULLTEXT_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailId() {
    return mailId;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  @Column(name = "SENDER_NAME")
  public String getSenderName() {
    return senderName;
  }

  @Column(name = "SENDER_ENNAME")
  public String getSenderEnName() {
    return senderEnName;
  }

  @Column(name = "SENDER_TITLOGO")
  public String getSenderTitLogo() {
    return senderTitLogo;
  }

  @Column(name = "SENDER_AVATAR")
  public String getSenderAvatar() {
    return senderAvatar;
  }

  @Column(name = "MAIL_TITLE")
  public String getMailTitle() {
    return mailTitle;
  }

  @Column(name = "MAIL_ENTITLE")
  public String getMailEnTitle() {
    return mailEnTitle;
  }

  @Column(name = "PARAM_JSON")
  public String getParamJson() {
    return paramJson;
  }

  @Column(name = "SEND_DATE")
  public Date getSendDate() {
    return sendDate;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  @Transient
  public Map<String, String> getParamMap() {
    if (paramJson != null) {
      paramMap = JacksonUtils.jsonMapUnSerializer(paramJson);
    }
    return paramMap;
  }

  @Transient
  public String getDes3MailId() {
    if (mailId != null) {
      des3MailId = ServiceUtil.encodeToDes3(mailId + "");
    }
    return des3MailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public void setSenderEnName(String senderEnName) {
    this.senderEnName = senderEnName;
  }

  public void setSenderTitLogo(String senderTitLogo) {
    this.senderTitLogo = senderTitLogo;
  }

  public void setSenderAvatar(String senderAvatar) {
    this.senderAvatar = senderAvatar;
  }

  public void setMailTitle(String mailTitle) {
    this.mailTitle = mailTitle;
  }

  public void setMailEnTitle(String mailEnTitle) {
    this.mailEnTitle = mailEnTitle;
  }

  public void setParamJson(String paramJson) {
    this.paramJson = paramJson;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setParamMap(Map<String, String> paramMap) {
    this.paramMap = paramMap;
  }

  public void setDes3MailId(String des3MailId) {
    this.des3MailId = des3MailId;
  }
}
