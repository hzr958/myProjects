package com.smate.center.task.exception;

/**
 * DAO层异常报告.
 * 
 * @author zb
 *
 */
public class DaoException extends Exception {
  private static final long serialVersionUID = 8663831440217049102L;

  public DaoException() {
    super();
  }

  public DaoException(String arg0) {
    super(arg0);
  }

  public DaoException(Throwable arg1) {
    super(arg1);
  }

  public DaoException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
