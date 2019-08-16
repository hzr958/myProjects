package com.smate.center.task.exception;

public class SingleTaskException extends TaskException {

  /**
   * 临时任务异常
   * 
   */
  private static final long serialVersionUID = 7167945673704944807L;

  public SingleTaskException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public SingleTaskException(String arg0) {
    super(arg0);
  }

  public SingleTaskException(Throwable arg1) {
    super(arg1);
  }



}
