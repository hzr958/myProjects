package com.smate.center.open.exception;

/**
 * 
 * 从数据库获取open人员关联对象异常!
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenDataGetOpenUserUnionException extends OpenSysDataException {


  /**
   * 
   */
  private static final long serialVersionUID = 8540214390026632660L;

  public OpenDataGetOpenUserUnionException() {
    super();
  }

  public OpenDataGetOpenUserUnionException(String arg0) {
    super(arg0);
  }

  public OpenDataGetOpenUserUnionException(Throwable arg1) {
    super(arg1);
  }

  public OpenDataGetOpenUserUnionException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
