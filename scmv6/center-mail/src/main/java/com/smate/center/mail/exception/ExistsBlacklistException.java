package com.smate.center.mail.exception;

/**
 * 存在于黑名单上
 * 
 * @author tsz
 *
 */
public class ExistsBlacklistException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "接收者 存在于黑名单->";

  public ExistsBlacklistException() {
    super(msg);
  }

  public ExistsBlacklistException(String arg0) {
    super(msg + arg0);
  }

  public ExistsBlacklistException(Throwable arg1) {
    super(msg, arg1);
  }

  public ExistsBlacklistException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
