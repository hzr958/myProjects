package com.smate.center.mail.exception;

/**
 * 
 * 邮件系统异常－父级
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class MailException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  private static final String msg = "邮件服务异常->";

  public MailException() {
    super(msg);
  }

  public MailException(String arg0) {
    super(msg + arg0);
  }

  public MailException(Throwable arg1) {
    super(msg, arg1);
  }

  public MailException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
