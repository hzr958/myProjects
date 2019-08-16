package com.smate.center.mail.exception;

/**
 * 用户不接收此类邮件异常
 * 
 * @author zzx
 *
 */
public class NotReceiveException extends MailException {
  private static final long serialVersionUID = -7570297986830829586L;
  private static final String msg = "用户设置不接收此类邮件->";

  public NotReceiveException() {
    super(msg);
  }

  public NotReceiveException(String arg0) {
    super(msg + arg0);
  }

  public NotReceiveException(Throwable arg1) {
    super(msg, arg1);
  }

  public NotReceiveException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
