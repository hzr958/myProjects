package com.smate.center.mail.exception;

/**
 * 非生产机 不在白名单上
 * 
 * @author tsz
 *
 */
public class NotExistsWhitelistException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "不存在于白名单上->";

  public NotExistsWhitelistException() {
    super(msg);
  }

  public NotExistsWhitelistException(String arg0) {
    super(msg + arg0);
  }

  public NotExistsWhitelistException(Throwable arg1) {
    super(msg, arg1);
  }

  public NotExistsWhitelistException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
