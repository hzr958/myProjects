package com.smate.web.group.exception;


/**
 * 
 * 群组参数错误异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class GroupParameterException extends GroupException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;


  public GroupParameterException() {
    super();
  }

  public GroupParameterException(String arg0) {
    super(arg0);
  }

  public GroupParameterException(Throwable arg1) {
    super(arg1);
  }

  public GroupParameterException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
