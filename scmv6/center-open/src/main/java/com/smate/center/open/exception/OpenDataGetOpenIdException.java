package com.smate.center.open.exception;

/**
 * 
 * 从数据库获取openId异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenDataGetOpenIdException extends OpenSysDataException {


  /**
   * 
   */
  private static final long serialVersionUID = 8540214390026632660L;

  public OpenDataGetOpenIdException() {
    super();
  }

  public OpenDataGetOpenIdException(String arg0) {
    super(arg0);
  }

  public OpenDataGetOpenIdException(Throwable arg1) {
    super(arg1);
  }

  public OpenDataGetOpenIdException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
