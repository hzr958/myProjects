package com.smate.web.v8pub.exception;

/**
 * 成果处理器异常
 * 
 * @author tsz
 *
 * @date 2018年6月7日
 */
public class PubHandlerException extends ServiceException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PubHandlerException() {
    super("成果处理器异常");
  }

  public PubHandlerException(String message) {
    super("成果处理器异常 >>" + message);
  }

  public PubHandlerException(String message, Throwable cause) {
    super("成果处理器异常 >>" + message, cause);
  }

  public PubHandlerException(Throwable cause) {
    super(cause);
  }

  public PubHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super("成果处理器异常 >>" + message, cause, enableSuppression, writableStackTrace);
  }
}
