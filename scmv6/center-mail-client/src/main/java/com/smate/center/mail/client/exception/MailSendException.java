package com.smate.center.mail.client.exception;

/**
 * 邮件发送异常
 * 
 * @author tsz
 *
 */
public class MailSendException extends MailClientException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "邮件发送异常->";

  public MailSendException() {
    super(msg);
  }

  public MailSendException(String arg0) {
    super(msg + arg0);
  }

  public MailSendException(Throwable arg1) {
    super(msg, arg1);
  }

  public MailSendException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
