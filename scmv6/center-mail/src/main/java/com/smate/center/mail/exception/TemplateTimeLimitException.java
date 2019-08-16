package com.smate.center.mail.exception;

/**
 * 模版发送频率限制、时间限制
 * 
 * @author zzx
 *
 */
public class TemplateTimeLimitException extends MailException {
  private static final long serialVersionUID = 1L;
  private static final String msg = "模版发送频率限制异常->";

  public TemplateTimeLimitException() {
    super(msg);
  }

  public TemplateTimeLimitException(String arg0) {
    super(msg + arg0);
  }

  public TemplateTimeLimitException(Throwable arg1) {
    super(msg, arg1);
  }

  public TemplateTimeLimitException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }
}
