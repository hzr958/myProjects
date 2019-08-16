package com.smate.center.open.exception;

/**
 * open解密失败
 * 
 * @author tsz
 *
 */
public class OpenDecodeException extends OpenException {

  private static final long serialVersionUID = 6835106633863826440L;

  public OpenDecodeException() {
    super();
  }


  public OpenDecodeException(String arg0) {
    super(arg0);
  }

  public OpenDecodeException(Throwable arg1) {
    super(arg1);
  }

  public OpenDecodeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
