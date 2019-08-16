package com.smate.core.base.utils.exception;


/**
 * 业务层操作异常.
 * 
 * @author zb
 *
 */
public class SysServiceException extends Exception {
  private static final long serialVersionUID = 988592186626034036L;

  public SysServiceException() {
    super();
  }

  public SysServiceException(String arg0) {
    super(arg0);
  }

  public SysServiceException(Throwable arg1) {
    super(arg1);
  }

  public SysServiceException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
