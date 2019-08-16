package com.smate.center.mail.exception;

/**
 * 找不到邮件模版异常
 * 
 * @author zzx
 *
 */
public class NotTemplateException extends MailException {
  private static final long serialVersionUID = -7570297986830829586L;
  private static final String msg = "找不到邮件模版异常->";

  public NotTemplateException() {
    super(msg);
  }

  public NotTemplateException(String arg0) {
    super(msg + arg0);
  }

  public NotTemplateException(Throwable arg1) {
    super(msg, arg1);
  }

  public NotTemplateException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
