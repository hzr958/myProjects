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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * 站内邀请发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "INVITE_MAILBOX")
public class PsnInviteMailBox implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -7249813969315836751L;
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
  private Integer status;
  private String zhReceiver;
  private String enReceiver;
  private Map<Integer, String> syncNodePsn;
  private List<InviteInbox> inboxs = new ArrayList<InviteInbox>();
  // 邮件处理状态
  private Integer optStatus;
  private Integer inviteType;
  // 只读的title
  private String readOnlyTitle;

  // 头衔
  private String titolo;
  // 首要单位
  private String primaryUtil;
  // 工作部门
  private String dept;
  // 职位
  private String position;
  // 发送者基本消息
  private String senderInfo;
  // 子标题，显示在每条邀请明细的邀请者后面
  private String childTitle;
  // 组名
  private String groupName;
  // 组图标
  private String groupImgURL;
  // 组首页
  private String groupUrl;
  // 组描述
  private String groupDesc;
  // 扩展字段,保存一些其他信息，以json格式保存
  private String ExtOtherInfo;
  private String viewTitle;

  public PsnInviteMailBox() {
    super();
  }

  public PsnInviteMailBox(String title, String content, Date optDate, Integer status) {
    super();

    this.title = title;
    this.content = content;
    this.optDate = optDate;
    this.status = status;
  }

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INVITE_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  @Transient
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Transient
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Transient
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

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "MAIL_ID", insertable = false, updatable = false)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id ASC")
  public List<InviteInbox> getInboxs() {
    return inboxs;
  }

  public void setInboxs(List<InviteInbox> inboxs) {
    this.inboxs = inboxs;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
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

  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  @Column(name = "PSN_HEAD_URL")
  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "INVITE_TYPE")
  public Integer getInviteType() {
    return inviteType;
  }

  public void setInviteType(Integer inviteType) {
    this.inviteType = inviteType;
  }

  @Column(name = "SENDER_INFO")
  public String getSenderInfo() {
    return senderInfo;
  }

  public void setSenderInfo(String senderInfo) {
    this.senderInfo = senderInfo;
  }

  @Transient
  public String getExtOtherInfo() {
    return ExtOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    ExtOtherInfo = extOtherInfo;
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
  public Integer getOptStatus() {
    return optStatus;
  }

  public void setOptStatus(Integer optStatus) {
    this.optStatus = optStatus;
  }

  @Transient
  public String getReadOnlyTitle() {
    return readOnlyTitle;
  }

  public void setReadOnlyTitle(String readOnlyTitle) {
    this.readOnlyTitle = readOnlyTitle;
  }

  @Transient
  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    if (titolo == null || titolo.trim().toLowerCase().equals("null")) {
      this.titolo = "";
    } else {
      this.titolo = titolo;
    }
  }

  @Transient
  public String getPrimaryUtil() {
    return primaryUtil;
  }

  public void setPrimaryUtil(String primaryUtil) {
    if (primaryUtil == null || primaryUtil.trim().toLowerCase().equals("null")) {
      this.primaryUtil = "";
    } else {
      this.primaryUtil = primaryUtil;
    }
  }

  @Transient
  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    if (dept == null || dept.trim().toLowerCase().equals("null")) {
      this.dept = "";
    } else {
      this.dept = dept;
    }
  }

  @Transient
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    if (position == null || position.trim().toLowerCase().equals("null")) {
      this.position = "";
    } else {
      this.position = position;
    }
  }

  @Transient
  public String getChildTitle() {
    this.getViewTitle();
    this.childTitle = this.viewTitle;
    // if (this.viewTitle != null && psnName != null)
    // childTitle = this.viewTitle.replace(psnName, "");
    return childTitle;
  }

  public void setChildTitle(String childTitle) {
    this.childTitle = childTitle;
  }

  @Transient
  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  @Transient
  public String getGroupImgURL() {
    return groupImgURL;
  }

  public void setGroupImgURL(String groupImgURL) {
    this.groupImgURL = groupImgURL;
  }

  @Transient
  public String getGroupUrl() {
    return groupUrl;
  }

  public void setGroupUrl(String groupUrl) {
    this.groupUrl = groupUrl;
  }

  @Transient
  public String getGroupDesc() {
    return groupDesc;
  }

  public void setGroupDesc(String groupDesc) {
    this.groupDesc = groupDesc;
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

  public void setViewTitle(String viewTitle) {
    this.viewTitle = viewTitle;
  }

}
