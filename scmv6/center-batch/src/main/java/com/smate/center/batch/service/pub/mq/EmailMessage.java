package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 邮件发送model.
 * 
 * @author liqinghua
 *
 */
public class EmailMessage implements Serializable {


  /**
   *
   */
  private static final long serialVersionUID = 1227653584069384539L;
  private String fromAddress;
  private String toAddress;
  private String title;
  private String mailTemplate;
  private Long opPsnId;
  private HashMap<String, String> params;
  private String[] attachments;
  private String[] attachmentNames;

  public EmailMessage(String fromAddress, String toAddress, String title, String mailTemplate, Long opPsnId,
      HashMap<String, String> params) {
    super();
    this.fromAddress = fromAddress;
    this.toAddress = toAddress;
    this.title = title;
    this.mailTemplate = mailTemplate;
    this.opPsnId = opPsnId;
    this.params = params;
  }

  public EmailMessage(String fromAddress, String toAddress, String title, String mailTemplate, Long opPsnId,
      String[] attachments, String[] attachmentNames, HashMap<String, String> params) {
    super();
    this.fromAddress = fromAddress;
    this.toAddress = toAddress;
    this.title = title;
    this.mailTemplate = mailTemplate;
    this.opPsnId = opPsnId;
    this.params = params;
    this.attachments = attachments;
    this.attachmentNames = attachmentNames;
  }



  public String getFromAddress() {
    return fromAddress;
  }

  public String getToAddress() {
    return toAddress;
  }

  public String getTitle() {
    return title;
  }

  public String getMailTemplate() {
    return mailTemplate;
  }

  public Long getOpPsnId() {
    return opPsnId;
  }

  public HashMap<String, String> getParams() {
    return params;
  }

  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setMailTemplate(String mailTemplate) {
    this.mailTemplate = mailTemplate;
  }

  public void setOpPsnId(Long opPsnId) {
    this.opPsnId = opPsnId;
  }

  public void setParams(HashMap<String, String> params) {
    this.params = params;
  }

  public String[] getAttachments() {
    return attachments;
  }

  public void setAttachments(String[] attachments) {
    this.attachments = attachments;
  }

  public String[] getAttachmentNames() {
    return attachmentNames;
  }

  public void setAttachmentNames(String[] attachmentNames) {
    this.attachmentNames = attachmentNames;
  }



}
