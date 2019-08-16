package com.smate.web.group.exception;


/**
 * 
 * 群组异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class GroupException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;


  public GroupException() {
    super();
  }

  public GroupException(String arg0) {
    super(arg0);
  }

  public GroupException(Throwable arg1) {
    super(arg1);
  }

  public GroupException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
