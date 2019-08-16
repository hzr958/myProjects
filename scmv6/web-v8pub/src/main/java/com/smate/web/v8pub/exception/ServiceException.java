package com.smate.web.v8pub.exception;

/**
 * 服务异常类
 * 
 * @author houchuanjie
 * @date 2018/05/31 16:13
 */
public class ServiceException extends RuntimeException {

  public ServiceException() {}

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
