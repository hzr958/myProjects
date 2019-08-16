package com.smate.center.data.exception;

/**
 * 任务相关异常类
 * 
 * @author zk
 *
 */
public class TaskException extends Exception {

  private static final long serialVersionUID = -2369149131184566478L;

  public TaskException() {
    super();
  }

  public TaskException(String arg0) {
    super(arg0);
  }

  public TaskException(Throwable arg1) {
    super(arg1);
  }

  public TaskException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
