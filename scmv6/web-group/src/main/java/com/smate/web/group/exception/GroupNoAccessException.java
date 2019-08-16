package com.smate.web.group.exception;


/**
 * 
 * 没有对应的访问权限
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class GroupNoAccessException extends GroupException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;


  public GroupNoAccessException() {
    super();
  }

  public GroupNoAccessException(String arg0) {
    super(arg0);
  }

  public GroupNoAccessException(Throwable arg1) {
    super(arg1);
  }

  public GroupNoAccessException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
