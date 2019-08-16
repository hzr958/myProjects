package com.smate.center.oauth.exception;

/**
 * 注册异常
 *
 * @author wsn
 *
 */
public class RegisterException extends OauthException {

  /**
   * 
   */
  private static final long serialVersionUID = -2184277868961132840L;

  public RegisterException() {
    super();
  }

  public RegisterException(String arg0) {
    super(arg0);
  }

  public RegisterException(Throwable arg1) {
    super(arg1);
  }

  public RegisterException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
