/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.common.HtmlUtils;


/**
 * 站内消息发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "INSIDE_MAILBOX")
public class PsnInsideMailBox implements Serializable {


  private Long mailId;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String title;
  private String enTitle;
  private String content;
  private Date optDate;
  private String strOptDate;
  private Integer status;
  // 扩展字段，以json格式存储
  private String extOtherInfo;
  // 消息类型：1 好友评价
  private Integer msgType;
  // 接受者中文名
  private String zhReceiver;
  // 接受者英文名
  private String enReceiver;
  private Map<Integer, String> syncNodePsn;
  private List<InsideInbox> inboxs = new ArrayList<InsideInbox>();
  private String forwardCtn;
  private String strContent;
  // 请求信息
  private String requestMessage;
  // 附件名称
  private String insideFileName;
  // 附件加密id
  private String insideDes3FileId;
  // 附件id
  private String insideFileId;
  // 节点
  private Integer nodeId;
  // 多列表文件时的节点
  private String nodeIds;
  // 关系
  private String psnRelation;
  // 工作经历
  private String psnWork;
  // 评价内容
  private String evaContent;
  private String noHtmlContent;
  private String viewTitle;

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSIDE_MAILBOX", allocationSize = 1)
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

  @Column(name = "TITLE_EN")
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

  @Column(name = "OPT_DATE")
  public Date getOptDate() {
    return optDate;
  }

  public void setOptDate(Date optDate) {
    this.optDate = optDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "MAIL_ID", insertable = false, updatable = false)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id ASC")
  public List<InsideInbox> getInboxs() {
    return inboxs;
  }

  public void setInboxs(List<InsideInbox> inboxs) {
    this.inboxs = inboxs;
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

  @Column(name = "REQUEST_MESSAGE")
  public String getRequestMessage() {
    return requestMessage;
  }

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  @Column(name = "MSG_TYPE")
  public Integer getMsgType() {
    return msgType;
  }

  public void setMsgType(Integer msgType) {
    this.msgType = msgType;
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

  @Transient
  public Map<Integer, String> getSyncNodePsn() {
    return syncNodePsn;
  }

  public void setSyncNodePsn(Map<Integer, String> syncNodePsn) {
    this.syncNodePsn = syncNodePsn;
  }

  @Transient
  public String getForwardCtn() {
    return forwardCtn;
  }

  public void setForwardCtn(String forwardCtn) {
    this.forwardCtn = forwardCtn;
  }

  @Transient
  public String getStrContent() {
    return strContent;
  }

  public void setStrContent(String strContent) {
    this.strContent = strContent;
  }

  @Transient
  public String getInsideFileName() {
    return insideFileName;
  }

  public void setInsideFileName(String insideFileName) {
    this.insideFileName = insideFileName;
  }

  @Transient
  public String getInsideDes3FileId() {
    return insideDes3FileId;
  }

  public void setInsideDes3FileId(String insideDes3FileId) {
    this.insideDes3FileId = insideDes3FileId;
  }

  @Transient
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Transient
  public String getInsideFileId() {
    return insideFileId;
  }

  public void setInsideFileId(String insideFileId) {
    this.insideFileId = insideFileId;
  }

  @Transient
  public String getStrOptDate() {
    if (optDate != null)
      return DateFormatUtils.format(optDate, "yyyy-MM-dd HH:mm:ss");
    return strOptDate;
  }

  public void setStrOptDate(String strOptDate) {
    this.strOptDate = strOptDate;
  }

  @Transient
  public String getPsnRelation() {
    return psnRelation;
  }

  public void setPsnRelation(String psnRelation) {
    this.psnRelation = psnRelation;
  }

  @Transient
  public String getPsnWork() {
    return psnWork;
  }

  public void setPsnWork(String psnWork) {
    this.psnWork = psnWork;
  }

  @Transient
  public String getEvaContent() {
    return evaContent;
  }

  public void setEvaContent(String evaContent) {
    this.evaContent = evaContent;
  }

  @Transient
  public String getNodeIds() {
    return nodeIds;
  }

  public void setNodeIds(String nodeIds) {
    this.nodeIds = nodeIds;
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
  public String getViewTitle() {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(locale)) {
      viewTitle = StringUtils.isBlank(title) ? enTitle : title;
    } else {
      viewTitle = StringUtils.isBlank(enTitle) ? title : enTitle;
    }
    return viewTitle;
  }
}
