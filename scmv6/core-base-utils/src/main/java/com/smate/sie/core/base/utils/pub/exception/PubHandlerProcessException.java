package com.smate.sie.core.base.utils.pub.exception;

/**
 * 成果处理器->过程异常
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public class PubHandlerProcessException extends PubHandlerException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PubHandlerProcessException() {
    super("组装处理异常");
  }

  public PubHandlerProcessException(String message) {
    super("组装处理异常>>" + message);
  }

  public PubHandlerProcessException(String message, Throwable cause) {
    super("组装处理异常>>" + message, cause);
  }

  public PubHandlerProcessException(Throwable cause) {
    super(cause);
  }

  public PubHandlerProcessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super("组装处理异常 >>" + message, cause, enableSuppression, writableStackTrace);
  }
}
