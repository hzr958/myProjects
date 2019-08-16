package com.smate.center.job.framework.exception;

/**
 * 无法停止任务的异常
 *
 * @author Created by houchuanjie
 * @date 2018/06/21 11:24
 */
public class CouldNotStopJobException extends RuntimeException {

  private static final long serialVersionUID = -4660372915191862155L;

  public CouldNotStopJobException() {
    super();
  }

  public CouldNotStopJobException(String message) {
    super(message);
  }

  public CouldNotStopJobException(String message, Throwable cause) {
    super(message, cause);
  }

  public CouldNotStopJobException(Throwable cause) {
    super(cause);
  }

  protected CouldNotStopJobException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
