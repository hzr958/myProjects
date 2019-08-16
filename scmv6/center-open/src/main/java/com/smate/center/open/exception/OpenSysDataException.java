package com.smate.center.open.exception;

/**
 * 
 * 数据库异常类
 * 
 * @author hzr
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenSysDataException extends OpenException {


  /**
   * 
   */
  private static final long serialVersionUID = 8540214390026632660L;

  public OpenSysDataException() {
    super();
  }

  public OpenSysDataException(String arg0) {
    super(arg0);
  }

  public OpenSysDataException(Throwable arg1) {
    super(arg1);
  }

  public OpenSysDataException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
