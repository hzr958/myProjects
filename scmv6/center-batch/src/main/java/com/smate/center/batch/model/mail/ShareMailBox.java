/**
 * 
 */
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

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.common.HtmlUtils;

/**
 * 站内消息发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "SHARE_MAILBOX")
public class ShareMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7160867825461656666L;
  private Long mailId;
  // 发件人
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String title;
  private String enTitle;
  private String content;
  // 发件时间
  private Date sendDate;
  // 共享有效期限
  private Date deadLine;
  // 发件人状态

  private Integer status;
  private String zhReceiver;
  private String enReceiver;
  // 同步人员节点
  private Map<Integer, String> syncNodePsn;
  /** 关联表PSN_RES_SEND字段RES_SEND_ID. */
  private Long resSendId;
  /** 扩展字段，以json格式存储. */
  private String extOtherInfo;
  /** 分享类型. */
  private Integer shareType;
  /** 消息中心查看模板. */
  private Integer tmpId;
  /** 是否是老数据. **/
  private Integer oldData = 0;

  /** 显示标题. */
  private String viewTitle;
  /** 去除html标签的内容. */
  private String noHtmlContent;
  /** 消息接收人id（只取一个）. */
  private Long receiverId;
  /** 消息接收人姓名（只取一个）. */
  private String receiverName;
  /** 消息接收人头像（只取一个）. */
  private String receiverAvatars;
  /** 消息接收人数. */
  private int receiverNum = 0;

  // 分享状态：1.取消分享
  private Integer shareStatus = 0;

  public ShareMailBox() {
    super();
  }

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SHARE_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "OPT_DATE")
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date optDate) {
    this.sendDate = optDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "DEADLINE")
  public Date getDeadLine() {
    return deadLine;
  }

  public void setDeadLine(Date deadLine) {
    this.deadLine = deadLine;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "PSN_HEAD_URL")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @Column(name = "ZH_RECEIVER")
  public String getZhReceiver() {
    return zhReceiver;
  }

  public void setZhReceiver(String zhReceiver) {
    this.zhReceiver = zhReceiver;
  }

  @Column(name = "EN_RECEIVER")
  public String getEnReceiver() {
    return enReceiver;
  }

  public void setEnReceiver(String enReceiver) {
    this.enReceiver = enReceiver;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "RES_SEND_ID")
  public Long getResSendId() {
    return resSendId;
  }

  public void setResSendId(Long resSendId) {
    this.resSendId = resSendId;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  @Column(name = "SHARE_TYPE")
  public Integer getShareType() {
    return shareType;
  }

  public void setShareType(Integer shareType) {
    this.shareType = shareType;
  }

  @Column(name = "TMP_ID")
  public Integer getTmpId() {
    return tmpId;
  }

  public void setTmpId(Integer tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "OLD_DATA")
  public Integer getOldData() {
    return oldData;
  }

  public void setOldData(Integer oldData) {
    this.oldData = oldData;
  }

  @Transient
  public Map<Integer, String> getSyncNodePsn() {
    return syncNodePsn;
  }

  public void setSyncNodePsn(Map<Integer, String> syncNodePsn) {
    this.syncNodePsn = syncNodePsn;
  }

  @Transient
  public String getViewTitle() {
    if (StringUtils.isBlank(viewTitle)) {
      String locale = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(locale)) {
        viewTitle = (this.oldData == 1 ? "" : psnName) + (StringUtils.isNotBlank(title) ? title : enTitle);
      } else {
        viewTitle = (this.oldData == 1 ? "" : psnName) + " " + (StringUtils.isNotBlank(enTitle) ? enTitle : title);
      }
    }

    return viewTitle;
  }

  public void setViewTitle(String viewTitle) {
    this.viewTitle = viewTitle;
  }

  @Transient
  public String getNoHtmlContent() {
    noHtmlContent = this.content;
    if (noHtmlContent != null) {
      noHtmlContent = HtmlUtils.Html2Text(noHtmlContent);
    }
    return noHtmlContent;
  }

  public void setNoHtmlContent(String noHtmlContent) {
    this.noHtmlContent = noHtmlContent;
  }

  @Transient
  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  @Transient
  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  @Transient
  public String getReceiverAvatars() {
    return receiverAvatars;
  }

  public void setReceiverAvatars(String receiverAvatars) {
    this.receiverAvatars = receiverAvatars;
  }

  @Transient
  public int getReceiverNum() {
    return receiverNum;
  }

  public void setReceiverNum(int receiverNum) {
    this.receiverNum = receiverNum;
  }

  @Column(name = "SHARE_STATUS")
  public Integer getShareStatus() {
    return shareStatus;
  }

  public void setShareStatus(Integer shareStatus) {
    this.shareStatus = shareStatus;
  }



}
