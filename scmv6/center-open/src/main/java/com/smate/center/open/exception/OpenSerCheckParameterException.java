package com.smate.center.open.exception;

/**
 * 
 * 获取信息参数验证异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenSerCheckParameterException extends OpenException {


  /**
   * 
   */
  private static final long serialVersionUID = 8540214390026632660L;

  public OpenSerCheckParameterException() {
    super();
  }

  public OpenSerCheckParameterException(String arg0) {
    super(arg0);
  }

  public OpenSerCheckParameterException(Throwable arg1) {
    super(arg1);
  }

  public OpenSerCheckParameterException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
