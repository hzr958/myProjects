package com.smate.sie.web.application.model.validate;

import java.io.Serializable;

/**
 * MailNotficationList表邮件通知Key常量类
 * 
 * @author xr
 *
 */
public class MailNotificationKeys implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2576611755362023530L;

  // 新单位注册邮件通知管理员审核key
  public static final String NEW_REGISTER_INSTITUTION = "newRegisterInstitution";
  // 单位注册审核通过，邮件通知检索组key
  public static final String AUDIT_NOTIFICATE_SEARCH = "auditNotificateSearch";
  // 单位注册审核通过，邮件通知美工key
  public static final String AUDIT_NOTIFICATE_UI = "auditNotificateUI";
  // 开发票通知财务邮件key
  public static final String VALIDATE_NEED_INVOICE = "validateNeedInvoice";

}
