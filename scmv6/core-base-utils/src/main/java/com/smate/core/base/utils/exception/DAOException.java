package com.smate.core.base.utils.exception;

/**
 * 捕获运行时异常封装成DAO异常
 * 
 * @author houchuanjie
 * @date 2018/06/01 17:15
 */
public class DAOException extends RuntimeException {

  private static final long serialVersionUID = -2855986902614514299L;

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
