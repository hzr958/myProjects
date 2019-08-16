package com.smate.center.open.exception;

/**
 * 
 * open系统异常－父级
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  public OpenException() {
    super();
  }

  public OpenException(String arg0) {
    super(arg0);
  }

  public OpenException(Throwable arg1) {
    super(arg1);
  }

  public OpenException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
