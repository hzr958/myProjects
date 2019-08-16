package com.smate.center.mail.exception;

/**
 * 没有可用发送账号异常
 * 
 * @author tsz
 *
 */
public class NotExistsSenderException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "没有可用发件账号异常->";

  public NotExistsSenderException() {
    super(msg);
  }

  public NotExistsSenderException(String arg0) {
    super(msg + arg0);
  }

  public NotExistsSenderException(Throwable arg1) {
    super(msg, arg1);
  }

  public NotExistsSenderException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
