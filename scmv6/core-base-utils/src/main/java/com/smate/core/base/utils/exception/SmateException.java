package com.smate.core.base.utils.exception;

/**
 * 
 * 顶级异常类
 * 
 * @author zk
 *
 */
public class SmateException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 4633045119780446535L;

  public SmateException() {
    super();
  }

  public SmateException(String arg0) {
    super(arg0);
  }

  public SmateException(Throwable arg1) {
    super(arg1);
  }

  public SmateException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
