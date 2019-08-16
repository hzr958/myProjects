package com.smate.core.base.utils.exception;

/**
 * 动态相关异常
 * 
 * @author zk
 *
 */
public class DynException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 7192708716203555697L;

  public DynException() {
    super();
  }

  public DynException(String arg0) {
    super(arg0);
  }

  public DynException(Throwable arg1) {
    super(arg1);
  }

  public DynException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
