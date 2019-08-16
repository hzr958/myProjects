package com.smate.center.mail.exception;

/**
 * 低优先级
 * 
 * @author tsz
 *
 */
public class LowPriorException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "邮件优先级较低->";

  public LowPriorException() {
    super(msg);
  }

  public LowPriorException(String arg0) {
    super(msg + arg0);
  }

  public LowPriorException(Throwable arg1) {
    super(msg, arg1);
  }

  public LowPriorException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
