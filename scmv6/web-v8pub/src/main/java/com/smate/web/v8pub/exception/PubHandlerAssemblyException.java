package com.smate.web.v8pub.exception;

/**
 * 成果处理器->组装处理异常
 * 
 * @author tsz
 *
 * @date 2018年6月7日
 */
public class PubHandlerAssemblyException extends PubHandlerException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PubHandlerAssemblyException() {
    super("组装处理异常");
  }

  public PubHandlerAssemblyException(String message) {
    super("组装处理异常>>" + message);
  }

  public PubHandlerAssemblyException(String message, Throwable cause) {
    super("组装处理异常>>" + message, cause);
  }

  public PubHandlerAssemblyException(Throwable cause) {
    super(cause);
  }

  public PubHandlerAssemblyException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super("组装处理异常 >>" + message, cause, enableSuppression, writableStackTrace);
  }
}
