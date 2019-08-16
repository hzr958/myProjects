package com.smate.center.open.exception;

/**
 * 邮件存在异常
 */
public class EmailExistException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 7089293813133512917L;

  public EmailExistException() {
    super();
  }

  public EmailExistException(String arg0) {
    super(arg0);
  }

  public EmailExistException(Throwable arg1) {
    super(arg1);
  }

  public EmailExistException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
