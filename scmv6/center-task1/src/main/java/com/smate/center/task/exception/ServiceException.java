package com.smate.center.task.exception;

public class ServiceException extends RuntimeException {
  private static final long serialVersionUID = -4986840485823268301L;

  public ServiceException() {
    super();
  }

  public ServiceException(String arg0) {
    super(arg0);
  }

  public ServiceException(Throwable arg1) {
    super(arg1);
  }

  public ServiceException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
