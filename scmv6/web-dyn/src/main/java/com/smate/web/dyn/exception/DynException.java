package com.smate.web.dyn.exception;


/**
 * 
 * 动态异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class DynException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;


  public DynException() {
    super();
  }

  public DynException(String arg0) {
    super(arg0);
  }

  public DynException(Throwable arg1) {
    super(arg1);
  }

  public DynException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
