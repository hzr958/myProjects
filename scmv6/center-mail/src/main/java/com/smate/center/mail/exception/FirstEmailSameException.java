package com.smate.center.mail.exception;

/**
 * 模版发送频率限制、时间限制
 * 
 * @author zzx
 *
 */
public class FirstEmailSameException extends MailException {
  private static final long serialVersionUID = 1L;
  private static final String msg = "首要邮件一致->";

  public FirstEmailSameException() {
    super(msg);
  }

  public FirstEmailSameException(String arg0) {
    super(msg + arg0);
  }

  public FirstEmailSameException(Throwable arg1) {
    super(msg, arg1);
  }

  public FirstEmailSameException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
