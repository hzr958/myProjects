package com.smate.center.oauth.exception;

/**
 * 
 * oauth系统异常－父级
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OauthException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  public OauthException() {
    super();
  }

  public OauthException(String arg0) {
    super(arg0);
  }

  public OauthException(Throwable arg1) {
    super(arg1);
  }

  public OauthException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
