package com.smate.center.mail.exception;

/**
 * 邮件内容包含敏感词
 * 
 * @author yhx
 *
 */
public class IncludeSensitiveWordsException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "邮件内容异常->";

  public IncludeSensitiveWordsException() {
    super(msg);
  }

  public IncludeSensitiveWordsException(String arg0) {
    super(msg + arg0);
  }

  public IncludeSensitiveWordsException(Throwable arg1) {
    super(msg, arg1);
  }

  public IncludeSensitiveWordsException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
