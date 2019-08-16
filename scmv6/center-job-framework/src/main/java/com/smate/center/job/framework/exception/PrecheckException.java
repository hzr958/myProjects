package com.smate.center.job.framework.exception;

/**
 * 预检查异常
 */
public class PrecheckException extends Exception {

  public PrecheckException() {
    super();
  }

  public PrecheckException(String message) {
    super(message);
  }

  public PrecheckException(String message, Throwable cause) {
    super(message, cause);
  }

  public PrecheckException(Throwable cause) {
    super(cause);
  }

  protected PrecheckException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
