package com.smate.center.open.exception;

/**
 * 
 * 保存openId与用户的关联关系异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenDataSaveOpenUserUnionException extends OpenSysDataException {


  /**
   * 
   */
  private static final long serialVersionUID = 8540214390026632660L;

  public OpenDataSaveOpenUserUnionException() {
    super();
  }

  public OpenDataSaveOpenUserUnionException(String arg0) {
    super(arg0);
  }

  public OpenDataSaveOpenUserUnionException(Throwable arg1) {
    super(arg1);
  }

  public OpenDataSaveOpenUserUnionException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
