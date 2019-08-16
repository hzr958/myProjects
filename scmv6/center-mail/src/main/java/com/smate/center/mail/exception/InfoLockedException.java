package com.smate.center.mail.exception;

/**
 * 信息被锁定 异常
 * 
 * @author tsz
 *
 */
public class InfoLockedException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "信息被锁定->";

  public InfoLockedException() {
    super(msg);
  }

  public InfoLockedException(String arg0) {
    super(msg + arg0);
  }

  public InfoLockedException(Throwable arg1) {
    super(msg, arg1);
  }

  public InfoLockedException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
