package com.smate.sie.core.base.utils.pub.exception;

/**
 * 服务异常类
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public class ServiceException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7670073954429360937L;

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
