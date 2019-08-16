package com.smate.center.mail.connector.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 邮件每日统计表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MAIL_EVERYDAY_STATISTIC")
public class MailEverydayStatistic implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_MAIL_EVERYDAY_STATISTIC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "TOTAL_COUNT")
  private Integer totalCount = 0;// 总数
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "SEND_SUCCESS")
  private Integer sendSuccess = 0;// 发送成功
  @Column(name = "SEND_ERROR")
  private Integer sendError = 0;// 发送出错
  @Column(name = "TO_BE_CONSTRUCT")
  private Integer toBeConstruct = 0;// 待构造
  @Column(name = "CONSTRUCT_ERROR")
  private Integer constructError = 0;// 构造出错
  @Column(name = "REFUSE_RECEIVE")
  private Integer refuseReceive = 0;// 拒绝接收
  @Column(name = "TO_BE_DISTRIBUTED")
  private Integer toBeDistributed = 0;// 待分配
  @Column(name = "TO_BE_SEND")
  private Integer toBeSend = 0;// 待发送
  @Column(name = "DISPATCH_ERROR")
  private Integer dispatchError = 0;// 调度出错
  @Column(name = "BLACKLIST")
  private Integer blacklist = 0;// 黑名单
  @Column(name = "RECEIVER_INEXISTENCE")
  private Integer receiverInexistence = 0;// 收件箱不存在
  @Column(name = "MAIL_SEND_ERROR")
  private Integer mailSendError = 0;// 邮件发送出错
  @Column(name = "MAIL_DISPATCH_ERROR")
  private Integer mailDispatchError = 0;// 邮件调度出错
  @Column(name = "SENDING")
  private Integer sending = 0;// 正在发送
  @Column(name = "NO_WHITE_LIST")
  private Integer noWhiteList = 0;// 不在白名单
  @Column(name = "OTHER")
  private Integer other = 0;// 其他
  @Transient
  private String statisticsDateStartStr;
  @Transient
  private String statisticsDateEndStr;

  @Column(name = "TEMPLATE_TIME_LIMIT")
  private Integer templateTimeLimit = 0;// 模版发送频率限制
  @Column(name = "FIRST_EMAIL_SAME")
  private Integer firstEmailSame = 0;// 收件人与发件人首要邮箱一致

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getSendSuccess() {
    return sendSuccess;
  }

  public void setSendSuccess(Integer sendSuccess) {
    this.sendSuccess = sendSuccess;
  }

  public Integer getSendError() {
    return sendError;
  }

  public void setSendError(Integer sendError) {
    this.sendError = sendError;
  }

  public Integer getToBeConstruct() {
    return toBeConstruct;
  }

  public void setToBeConstruct(Integer toBeConstruct) {
    this.toBeConstruct = toBeConstruct;
  }

  public Integer getConstructError() {
    return constructError;
  }

  public void setConstructError(Integer constructError) {
    this.constructError = constructError;
  }

  public Integer getRefuseReceive() {
    return refuseReceive;
  }

  public void setRefuseReceive(Integer refuseReceive) {
    this.refuseReceive = refuseReceive;
  }

  public Integer getToBeDistributed() {
    return toBeDistributed;
  }

  public void setToBeDistributed(Integer toBeDistributed) {
    this.toBeDistributed = toBeDistributed;
  }

  public Integer getToBeSend() {
    return toBeSend;
  }

  public void setToBeSend(Integer toBeSend) {
    this.toBeSend = toBeSend;
  }

  public Integer getDispatchError() {
    return dispatchError;
  }

  public void setDispatchError(Integer dispatchError) {
    this.dispatchError = dispatchError;
  }

  public Integer getBlacklist() {
    return blacklist;
  }

  public void setBlacklist(Integer blacklist) {
    this.blacklist = blacklist;
  }

  public Integer getReceiverInexistence() {
    return receiverInexistence;
  }

  public void setReceiverInexistence(Integer receiverInexistence) {
    this.receiverInexistence = receiverInexistence;
  }

  public Integer getMailSendError() {
    return mailSendError;
  }

  public void setMailSendError(Integer mailSendError) {
    this.mailSendError = mailSendError;
  }

  public Integer getMailDispatchError() {
    return mailDispatchError;
  }

  public void setMailDispatchError(Integer mailDispatchError) {
    this.mailDispatchError = mailDispatchError;
  }

  public Integer getSending() {
    return sending;
  }

  public void setSending(Integer sending) {
    this.sending = sending;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getOther() {
    return other;
  }

  public void setOther(Integer other) {
    this.other = other;
  }

  public Integer getNoWhiteList() {
    return noWhiteList;
  }

  public void setNoWhiteList(Integer noWhiteList) {
    this.noWhiteList = noWhiteList;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public MailEverydayStatistic(Long id, Integer totalCount, Date createDate, Integer sendSuccess, Integer sendError,
      Integer toBeConstruct, Integer constructError, Integer refuseReceive, Integer toBeDistributed, Integer toBeSend,
      Integer dispatchError, Integer blacklist, Integer receiverInexistence, Integer mailSendError,
      Integer mailDispatchError, Integer sending, Integer noWhiteList, Integer other) {
    super();
    this.id = id;
    this.totalCount = totalCount;
    this.createDate = createDate;
    this.sendSuccess = sendSuccess;
    this.sendError = sendError;
    this.toBeConstruct = toBeConstruct;
    this.constructError = constructError;
    this.refuseReceive = refuseReceive;
    this.toBeDistributed = toBeDistributed;
    this.toBeSend = toBeSend;
    this.dispatchError = dispatchError;
    this.blacklist = blacklist;
    this.receiverInexistence = receiverInexistence;
    this.mailSendError = mailSendError;
    this.mailDispatchError = mailDispatchError;
    this.sending = sending;
    this.noWhiteList = noWhiteList;
    this.other = other;
  }

  public MailEverydayStatistic() {
    super();
  }

  public String getStatisticsDateStartStr() {
    return statisticsDateStartStr;
  }

  public void setStatisticsDateStartStr(String statisticsDateStartStr) {
    this.statisticsDateStartStr = statisticsDateStartStr;
  }

  public String getStatisticsDateEndStr() {
    return statisticsDateEndStr;
  }

  public void setStatisticsDateEndStr(String statisticsDateEndStr) {
    this.statisticsDateEndStr = statisticsDateEndStr;
  }

  public Integer getTemplateTimeLimit() {
    return templateTimeLimit;
  }

  public void setTemplateTimeLimit(Integer templateTimeLimit) {
    this.templateTimeLimit = templateTimeLimit;
  }

  public Integer getFirstEmailSame() {
    return firstEmailSame;
  }

  public void setFirstEmailSame(Integer firstEmailSame) {
    this.firstEmailSame = firstEmailSame;
  }

}
