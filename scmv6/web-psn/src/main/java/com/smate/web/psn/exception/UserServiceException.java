package com.smate.web.psn.exception;

public class UserServiceException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 4586358979752930335L;

  public UserServiceException() {
    super();
  }

  public UserServiceException(String arg0) {
    super(arg0);
  }

  public UserServiceException(Throwable arg1) {
    super(arg1);
  }

  public UserServiceException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
