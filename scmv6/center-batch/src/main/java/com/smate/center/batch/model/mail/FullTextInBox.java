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


/**
 * 全文请求收件箱.
 * 
 * @author cxr
 * 
 */
@Entity
@Table(name = "FULLTEXT_INBOX")
public class FullTextInBox implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 6724952537551052694L;
  private Long inboxId;// ID
  private Long mailId;// 发件箱ID
  private Long receiverId;// 接受人ID
  private String receiverName;// 接收人姓名
  private String receiverEnName;// 接收人英文名
  private String receiverTitLogo;// 接收人头衔
  private String receiverAvatar;// 接收人头像
  private String mailTitle;// 收件箱标题
  private String mailEnTitle;// 收件箱英文标题
  private String paramJson;// 参数
  private Date receiveDate;// 接收时间
  private int opStatus;// 操作状态：0：待操作；1：同意；2：拒绝; 3:资源已删除
  private int status;// 邮件状态：0：正常；1：删除
  private Map<String, String> paramMap;
  private boolean resIsDel;// 资源是否删除
  private int requestType; // 请求类型：１成果；２文献；３项目

  @Id
  @Column(name = "INBOX_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FULLTEXT_INBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getInboxId() {
    return inboxId;
  }

  @Column(name = "MAIL_ID")
  public Long getMailId() {
    return mailId;
  }

  @Column(name = "RECEIVER_ID")
  public Long getReceiverId() {
    return receiverId;
  }

  @Column(name = "RECEIVER_NAME")
  public String getReceiverName() {
    return receiverName;
  }

  @Column(name = "RECEIVER_ENNAME")
  public String getReceiverEnName() {
    return receiverEnName;
  }

  @Column(name = "RECEIVER_TITLOGO")
  public String getReceiverTitLogo() {
    return receiverTitLogo;
  }

  @Column(name = "RECEIVER_AVATAR")
  public String getReceiverAvatar() {
    return receiverAvatar;
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

  @Column(name = "RECEIVE_DATE")
  public Date getReceiveDate() {
    return receiveDate;
  }

  @Column(name = "OP_STATUS")
  public int getOpStatus() {
    return opStatus;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  @Transient
  public int getRequestType() {
    return requestType;
  }

  @Transient
  public boolean isResIsDel() {
    return resIsDel;
  }

  @Transient
  public Map<String, String> getParamMap() {
    if (paramJson != null) {
      paramMap = JacksonUtils.jsonMapUnSerializer(paramJson);
    }
    return paramMap;
  }

  public void setInboxId(Long inboxId) {
    this.inboxId = inboxId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public void setReceiverEnName(String receiverEnName) {
    this.receiverEnName = receiverEnName;
  }

  public void setReceiverTitLogo(String receiverTitLogo) {
    this.receiverTitLogo = receiverTitLogo;
  }

  public void setReceiverAvatar(String receiverAvatar) {
    this.receiverAvatar = receiverAvatar;
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

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
  }

  public void setOpStatus(int opStatus) {
    this.opStatus = opStatus;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setParamMap(Map<String, String> paramMap) {
    this.paramMap = paramMap;
  }

  public void setRequestType(int requestType) {
    this.requestType = requestType;
  }

  public void setResIsDel(boolean resIsDel) {
    this.resIsDel = resIsDel;
  }
}
