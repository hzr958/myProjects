package com.smate.web.management.model.mail;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.SearchMailInfo;
import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.management.model.mongodb.mail.MailReturnedRecord;

public class MailManageForm {
  private List<MailForShowInfo> mailForShowInfoList;
  private SearchMailInfo searchMailInfo;
  private Page<MailOriginalData> page;
  private String mailTemplateName;
  private String receiver;
  private String senderDateStartStr;
  private String senderDateEndStr;
  private DateFormat format;
  private Map<String, String> resultMap;
  private String statusFilter;
  private String des3MailId;
  private Long mailId; // 邮件id
  private List<MailLink> MailLinkList;
  private List<MailReturnedRecord> mailReturnedRecordList;
  private String account;
  private String address;
  private List<MailConnectError> mailConnectErrorList;
  private String showMailConnError;
  private String startSendDateStr;
  private String endSendDateStr;
  private Date startSendDate;
  private Date endSendDate;
  private String orderBy;// 排序
  private String sender;// 发件邮箱
  private Long receiverPsnId;// 收件人ID
  private Long senderPsnId;// 发件人ID

  public List<MailForShowInfo> getMailForShowInfoList() {
    return mailForShowInfoList;
  }

  public void setMailForShowInfoList(List<MailForShowInfo> mailForShowInfoList) {
    this.mailForShowInfoList = mailForShowInfoList;
  }

  public SearchMailInfo getSearchMailInfo() {
    if (searchMailInfo == null) {
      searchMailInfo = new SearchMailInfo();
      searchMailInfo.setMailTemplateName(mailTemplateName);
      searchMailInfo.setReceiver(receiver);
      searchMailInfo.setSenderDateStartStr(senderDateStartStr);
      searchMailInfo.setSenderDateEndStr(senderDateEndStr);
      searchMailInfo.setOrderBy(orderBy);
      searchMailInfo.setSender(sender);
      searchMailInfo.setReceiverPsnId(receiverPsnId);
      searchMailInfo.setSenderPsnId(senderPsnId);
      if (StringUtils.isNotBlank(statusFilter)) {
        // 字段对应MailManageConstant
        if (statusFilter.equals("to_be_construct")) {
          searchMailInfo.setStatus(0);
        }
        if (statusFilter.equals("construct_error")) {
          searchMailInfo.setStatus(2);
        }
        if (statusFilter.equals("receive_refuse")) {
          searchMailInfo.setStatus(3);
        }
        if (statusFilter.equals("frequency_limit")) {
          searchMailInfo.setStatus(4);
        }
        if (statusFilter.equals("to_be_distributed")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(0);
        }
        if (statusFilter.equals("to_be_sent")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(1);
        }
        if (statusFilter.equals("send_successfully")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(2);
        }
        if (statusFilter.equals("blacklist")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(3);
        }
        if (statusFilter.equals("receiver_inexistence")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(4);
        }
        if (statusFilter.equals("information_lock")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(7);
        }
        if (statusFilter.equals("send_error")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(8);
        }
        if (statusFilter.equals("scheduling_error")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(9);
        }
        if (statusFilter.equals("sending")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(10);
        }
        if (statusFilter.equals("build_send_error")) {
          searchMailInfo.setStatus(1);
          searchMailInfo.setSendStatus(11);
        }
      }
    }
    return searchMailInfo;
  }

  public void setSearchMailInfo(SearchMailInfo searchMailInfo) {
    this.searchMailInfo = searchMailInfo;
  }

  public Page<MailOriginalData> getPage() {
    return page;
  }

  public void setPage(Page<MailOriginalData> page) {
    this.page = page;
  }

  public String getMailTemplateName() {
    return mailTemplateName;
  }

  public void setMailTemplateName(String mailTemplateName) {
    this.mailTemplateName = mailTemplateName;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

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

  public DateFormat getFormat() {
    return format;
  }

  public void setFormat(DateFormat format) {
    this.format = format;
  }

  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public String getStatusFilter() {
    return statusFilter;
  }

  public void setStatusFilter(String statusFilter) {
    this.statusFilter = statusFilter;
  }

  public String getDes3MailId() {
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

  public List<MailLink> getMailLinkList() {
    return MailLinkList;
  }

  public void setMailLinkList(List<MailLink> mailLinkList) {
    MailLinkList = mailLinkList;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<MailReturnedRecord> getMailReturnedRecordList() {
    return mailReturnedRecordList;
  }

  public void setMailReturnedRecordList(List<MailReturnedRecord> mailReturnedRecordList) {
    this.mailReturnedRecordList = mailReturnedRecordList;
  }

  public List<MailConnectError> getMailConnectErrorList() {
    return mailConnectErrorList;
  }

  public void setMailConnectErrorList(List<MailConnectError> mailConnectErrorList) {
    this.mailConnectErrorList = mailConnectErrorList;
  }

  public String getShowMailConnError() {
    return showMailConnError;
  }

  public void setShowMailConnError(String showMailConnError) {
    this.showMailConnError = showMailConnError;
  }

  public String getEndSendDateStr() {
    return endSendDateStr;
  }

  public void setEndSendDateStr(String endSendDateStr) {
    this.endSendDateStr = endSendDateStr;
  }

  public String getStartSendDateStr() {
    return startSendDateStr;
  }

  public void setStartSendDateStr(String startSendDateStr) {
    this.startSendDateStr = startSendDateStr;
  }

  public Date getStartSendDate() {
    return startSendDate;
  }

  public void setStartSendDate(Date startSendDate) {
    this.startSendDate = startSendDate;
  }

  public Date getEndSendDate() {
    return endSendDate;
  }

  public void setEndSendDate(Date endSendDate) {
    this.endSendDate = endSendDate;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
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
