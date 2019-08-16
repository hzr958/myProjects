package com.smate.center.mail.connector.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 检索发送邮件记录
 * 
 * @author zzx
 *
 */
public class SearchMailInfo {
  private Long mailId; // 邮件id
  private String sender; // 发送者邮箱
  private String receiver; // 接收者邮箱
  private String mailClient; // 发送客户端
  private String subject; // 邮件主题
  private int mailTemplateCode; // 邮件模板
  private String mailTemplateName;// 模版名字
  private String priorLevel; // 优先级 A>B>C>D
  private Date createDate; // 创建时间
  private Date updateDate; // 更新时间
  private Date distributeDate; // 分配时间
  private Integer status; // 状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在
  private Integer sendStatus;
  private String msg;// 描述

  private List<Integer> templateCodeList;
  private String templateCodes;
  private String senderDateStartStr;
  private String senderDateEndStr;
  private Date senderDateStart;
  private Date senderDateEnd;
  private List<Map<String, Object>> statusList;
  private List<Map<String, Object>> sendStatusList;
  private String orderBy;// 排序
  private Long senderPsnId;// 发件人ID
  private Long receiverPsnId;// 收件人ID


  public String getSenderDateStartStr() {
    return senderDateStartStr;
  }

  public void setSenderDateStartStr(String senderDateStartStr) {
    this.senderDateStartStr = senderDateStartStr;
  }

  public String getSenderDateEndStr() {
    return senderDateEndStr;
  }

  public void setSenderDateEndStr(String senderDateEndStr) {
    this.senderDateEndStr = senderDateEndStr;
  }

  public Date getSenderDateStart() {
    return senderDateStart;
  }

  public void setSenderDateStart(Date senderDateStart) {
    this.senderDateStart = senderDateStart;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getMailClient() {
    return mailClient;
  }

  public void setMailClient(String mailClient) {
    this.mailClient = mailClient;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public int getMailTemplateCode() {
    return mailTemplateCode;
  }

  public void setMailTemplateCode(int mailTemplateCode) {
    this.mailTemplateCode = mailTemplateCode;
  }

  public String getMailTemplateName() {
    return mailTemplateName;
  }

  public void setMailTemplateName(String mailTemplateName) {
    this.mailTemplateName = mailTemplateName;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Date getDistributeDate() {
    return distributeDate;
  }

  public void setDistributeDate(Date distributeDate) {
    this.distributeDate = distributeDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getTemplateCodes() {
    if (StringUtils.isBlank(templateCodes) && templateCodeList != null && templateCodeList.size() > 0) {
      templateCodes = StringUtils.join(templateCodeList, ",");
    }
    return templateCodes;
  }

  public void setTemplateCodes(String templateCodes) {
    this.templateCodes = templateCodes;
  }

  public List<Integer> getTemplateCodeList() {
    return templateCodeList;
  }

  public void setTemplateCodeList(List<Integer> templateCodeList) {
    this.templateCodeList = templateCodeList;
  }

  public Date getSenderDateEnd() {
    return senderDateEnd;
  }

  public void setSenderDateEnd(Date senderDateEnd) {
    this.senderDateEnd = senderDateEnd;
  }

  public List<Map<String, Object>> getStatusList() {
    return statusList;
  }

  public void setStatusList(List<Map<String, Object>> statusList) {
    this.statusList = statusList;
  }

  public List<Map<String, Object>> getSendStatusList() {
    return sendStatusList;
  }

  public void setSendStatusList(List<Map<String, Object>> sendStatusList) {
    this.sendStatusList = sendStatusList;
  }

  public Integer getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Long getReceiverPsnId() {
    return receiverPsnId;
  }

  public void setReceiverPsnId(Long receiverPsnId) {
    this.receiverPsnId = receiverPsnId;
  }

  public Long getSenderPsnId() {
    return senderPsnId;
  }

  public void setSenderPsnId(Long senderPsnId) {
    this.senderPsnId = senderPsnId;
  }



}
