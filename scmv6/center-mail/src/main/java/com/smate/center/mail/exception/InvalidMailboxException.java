package com.smate.center.mail.exception;

@SuppressWarnings("serial")
public class InvalidMailboxException extends MailException {
  private static final String msg = "邮箱格式异常->";

  public InvalidMailboxException() {
    super(msg);
  }

  public InvalidMailboxException(String arg0) {
    super(msg + arg0);
  }

  public InvalidMailboxException(Throwable arg1) {
    super(msg, arg1);
  }

  public InvalidMailboxException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
