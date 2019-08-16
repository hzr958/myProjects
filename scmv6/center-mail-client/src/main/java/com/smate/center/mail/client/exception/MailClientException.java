package com.smate.center.mail.client.exception;

/**
 * 
 * 邮件客户端异常－父级
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class MailClientException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  private static final String msg = "邮件客户端->";

  public MailClientException() {
    super(msg);
  }

  public MailClientException(String arg0) {
    super(msg + arg0);
  }

  public MailClientException(Throwable arg1) {
    super(msg, arg1);
  }

  public MailClientException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
