package com.smate.core.base.exception;

/**
 * DAO异常包装类
 * 
 * @author houchuanjie
 * @date 2018/04/02 16:07
 */
public class DAOException extends RuntimeException {
  public DAOException() {}

  public DAOException(String message) {
    super(message);
  }

  public DAOException(String message, Throwable cause) {
    super(message, cause);
  }

  public DAOException(Throwable cause) {
    super(cause);
  }

  public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
