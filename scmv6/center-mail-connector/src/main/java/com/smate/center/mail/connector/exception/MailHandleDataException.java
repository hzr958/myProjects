package com.smate.center.mail.connector.exception;

/**
 * 邮件初始化数据异常
 * 
 * @author zzx
 *
 */
public class MailHandleDataException extends Exception {
  private static final long serialVersionUID = 3549748707210047517L;

  public MailHandleDataException() {
    super();
  }

  public MailHandleDataException(String arg0) {
    super(arg0);
  }

  public MailHandleDataException(Throwable arg1) {
    super(arg1);
  }

  public MailHandleDataException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
