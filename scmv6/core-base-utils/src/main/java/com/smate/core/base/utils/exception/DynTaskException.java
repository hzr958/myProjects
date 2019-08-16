package com.smate.core.base.utils.exception;

/**
 * 动态任务相关异常
 * 
 * @author hzr
 *
 */

public class DynTaskException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 5260055274627442197L;

  public DynTaskException() {
    super();
  }

  public DynTaskException(String arg0) {
    super(arg0);
  }

  public DynTaskException(Throwable arg1) {
    super(arg1);
  }

  public DynTaskException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
