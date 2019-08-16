package com.smate.sie.core.base.utils.pub.exception;


public class DuplicateCheckParameterException extends ServiceException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DuplicateCheckParameterException() {
    super("查重参数检查异常");
  }

  public DuplicateCheckParameterException(String message) {
    super("查重参数检查异常>>" + message);
  }

  public DuplicateCheckParameterException(String message, Throwable cause) {
    super("查重参数检查异常>>" + message, cause);
  }

  public DuplicateCheckParameterException(Throwable cause) {
    super(cause);
  }

  public DuplicateCheckParameterException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super("查重参数检查异常 >>" + message, cause, enableSuppression, writableStackTrace);
  }
}
