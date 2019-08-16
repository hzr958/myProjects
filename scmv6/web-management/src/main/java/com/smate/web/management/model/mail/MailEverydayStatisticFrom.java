package com.smate.web.management.model.mail;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.smate.center.mail.connector.model.MailEverydayStatistic;
import com.smate.core.base.utils.model.Page;

public class MailEverydayStatisticFrom {
  private Long id;// 主键
  private Integer totalCount = 0;// 总数
  private Date createDate;// 创建时间
  private Integer sendSuccess = 0;// 发送成功
  private Integer sendError = 0;// 发送出错
  private Integer toBeConstruct = 0;// 待构造
  private Integer constructError = 0;// 构造出错
  private Integer refuseReceive = 0;// 拒绝接收
  private Integer toBeDistributed = 0;// 待分配
  private Integer toBeSend = 0;// 待发送
  private Integer dispatchError = 0;// 调度出错
  private Integer blacklist = 0;// 黑名单
  private Integer receiverInexistence = 0;// 收件箱不存在
  private Integer mailSendError = 0;// 邮件发送出错
  private Integer mailDispatchError = 0;// 邮件调度出错
  private Integer sending = 0;// 正在发送
  private Integer noWhiteList = 0;// 不在白名单
  private Integer other = 0;// 其他
  private List<MailEverydayStatistic> mailStatisticList;
  private MailEverydayStatistic mailEverydayStatistic;
  private Page<MailEverydayStatistic> page;
  private String startStatisticsDateStr;
  private String endStatisticsDateStr;
  private DateFormat format;

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

  public MailEverydayStatisticFrom(Long id, Integer totalCount, Date createDate, Integer sendSuccess, Integer sendError,
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

  public MailEverydayStatisticFrom() {
    super();
  }

  public List<MailEverydayStatistic> getMailStatisticList() {
    return mailStatisticList;
  }

  public void setMailStatisticList(List<MailEverydayStatistic> mailStatisticList) {
    this.mailStatisticList = mailStatisticList;
  }

  public MailEverydayStatistic getMailEverydayStatistic() {
    if (mailEverydayStatistic == null) {
      mailEverydayStatistic = new MailEverydayStatistic();
      mailEverydayStatistic.setTotalCount(totalCount);
      mailEverydayStatistic.setBlacklist(blacklist);
      mailEverydayStatistic.setConstructError(constructError);
      mailEverydayStatistic.setCreateDate(createDate);
      mailEverydayStatistic.setDispatchError(dispatchError);
      mailEverydayStatistic.setId(id);
      mailEverydayStatistic.setMailDispatchError(mailDispatchError);
      mailEverydayStatistic.setMailSendError(mailSendError);
      mailEverydayStatistic.setNoWhiteList(noWhiteList);
      mailEverydayStatistic.setOther(other);
      mailEverydayStatistic.setReceiverInexistence(receiverInexistence);
      mailEverydayStatistic.setSendError(mailSendError);
      mailEverydayStatistic.setSending(sending);
      mailEverydayStatistic.setSendSuccess(sendSuccess);
      mailEverydayStatistic.setToBeConstruct(toBeConstruct);
      mailEverydayStatistic.setToBeDistributed(toBeDistributed);
      mailEverydayStatistic.setToBeSend(toBeSend);
      mailEverydayStatistic.setRefuseReceive(refuseReceive);
    }
    return mailEverydayStatistic;
  }

  public void setMailEverydayStatistic(MailEverydayStatistic mailEverydayStatistic) {
    this.mailEverydayStatistic = mailEverydayStatistic;
  }

  public Page<MailEverydayStatistic> getPage() {
    return page;
  }

  public void setPage(Page<MailEverydayStatistic> page) {
    this.page = page;
  }

  public String getStartStatisticsDateStr() {
    return startStatisticsDateStr;
  }

  public void setStartStatisticsDateStr(String startStatisticsDateStr) {
    this.startStatisticsDateStr = startStatisticsDateStr;
  }

  public String getEndStatisticsDateStr() {
    return endStatisticsDateStr;
  }

  public void setEndStatisticsDateStr(String endStatisticsDateStr) {
    this.endStatisticsDateStr = endStatisticsDateStr;
  }

  public DateFormat getFormat() {
    return format;
  }

  public void setFormat(DateFormat format) {
    this.format = format;
  }



}
