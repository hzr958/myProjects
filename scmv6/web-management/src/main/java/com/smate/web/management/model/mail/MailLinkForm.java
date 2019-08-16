package com.smate.web.management.model.mail;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.utils.model.Page;

public class MailLinkForm {
  private Page<MailLink> page;
  private List<MailLinkForm> MailLinkList;
  private Integer templateCode;
  private String templateName;
  private String urlDesc;
  private String linkKey;
  private Integer count;
  private MailLink mailLink;
  private String startCreateDateStr;
  private String endCreateDateStr;
  private DateFormat format;
  private Date startCreateDate;
  private Date endCreateDate;

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getUrlDesc() {
    return urlDesc;
  }

  public void setUrlDesc(String urlDesc) {
    this.urlDesc = urlDesc;
  }

  public String getLinkKey() {
    return linkKey;
  }

  public void setLinkKey(String linkKey) {
    this.linkKey = linkKey;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Page<MailLink> getPage() {
    return page;
  }

  public void setPage(Page<MailLink> page) {
    this.page = page;
  }

  public List<MailLinkForm> getMailLinkList() {
    return MailLinkList;
  }

  public void setMailLinkList(List<MailLinkForm> mailLinkList) {
    MailLinkList = mailLinkList;
  }

  public Integer getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(Integer templateCode) {
    this.templateCode = templateCode;
  }

  public MailLink getMailLink() {
    return mailLink;
  }

  public void setMailLink(MailLink mailLink) {
    this.mailLink = mailLink;
  }

  public String getStartCreateDateStr() {
    return startCreateDateStr;
  }

  public void setStartCreateDateStr(String startCreateDateStr) {
    this.startCreateDateStr = startCreateDateStr;
  }

  public String getEndCreateDateStr() {
    return endCreateDateStr;
  }

  public void setEndCreateDateStr(String endCreateDateStr) {
    this.endCreateDateStr = endCreateDateStr;
  }

  public DateFormat getFormat() {
    return format;
  }

  public void setFormat(DateFormat format) {
    this.format = format;
  }

  public Date getStartCreateDate() {
    return startCreateDate;
  }

  public void setStartCreateDate(Date startCreateDate) {
    this.startCreateDate = startCreateDate;
  }

  public Date getEndCreateDate() {
    return endCreateDate;
  }

  public void setEndCreateDate(Date endCreateDate) {
    this.endCreateDate = endCreateDate;
  }

}
