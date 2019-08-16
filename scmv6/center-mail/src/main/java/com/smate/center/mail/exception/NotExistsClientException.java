package com.smate.center.mail.exception;

/**
 * 没有可用客户端异常
 * 
 * @author tsz
 *
 */
public class NotExistsClientException extends MailException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "没有可用客户端异常->";

  public NotExistsClientException() {
    super(msg);
  }

  public NotExistsClientException(String arg0) {
    super(msg + arg0);
  }

  public NotExistsClientException(Throwable arg1) {
    super(msg, arg1);
  }

  public NotExistsClientException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
