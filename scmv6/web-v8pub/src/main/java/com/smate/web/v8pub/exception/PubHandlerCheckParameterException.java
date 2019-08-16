package com.smate.web.v8pub.exception;

/**
 * 成果处理器参数检查异常
 * 
 * @author tsz
 *
 * @date 2018年6月7日
 */
public class PubHandlerCheckParameterException extends PubHandlerException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PubHandlerCheckParameterException() {
    super("参数检查异常");
  }

  public PubHandlerCheckParameterException(String message) {
    super("参数检查异常>>" + message);
  }

  public PubHandlerCheckParameterException(String message, Throwable cause) {
    super("参数检查异常>>" + message, cause);
  }

  public PubHandlerCheckParameterException(Throwable cause) {
    super(cause);
  }

  public PubHandlerCheckParameterException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super("参数检查异常 >>" + message, cause, enableSuppression, writableStackTrace);
  }
}
