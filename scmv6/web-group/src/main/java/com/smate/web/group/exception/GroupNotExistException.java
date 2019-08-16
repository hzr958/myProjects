package com.smate.web.group.exception;


/**
 * 
 * 群组不存在异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class GroupNotExistException extends GroupException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;


  public GroupNotExistException() {
    super();
  }

  public GroupNotExistException(String arg0) {
    super(arg0);
  }

  public GroupNotExistException(Throwable arg1) {
    super(arg1);
  }

  public GroupNotExistException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
