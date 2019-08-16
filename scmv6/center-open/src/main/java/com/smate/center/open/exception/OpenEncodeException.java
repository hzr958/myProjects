package com.smate.center.open.exception;

/**
 * open加密失败
 * 
 * @author tsz
 *
 */
public class OpenEncodeException extends OpenException {

  private static final long serialVersionUID = 6835106633863826440L;

  public OpenEncodeException() {
    super();
  }


  public OpenEncodeException(String arg0) {
    super(arg0);
  }

  public OpenEncodeException(Throwable arg1) {
    super(arg1);
  }

  public OpenEncodeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
