package com.smate.center.task.exception;

public class TaskDupRecordException extends TaskException {
  /**
   * task任务重复记录异常
   */
  private static final long serialVersionUID = 4379631161361758118L;

  public TaskDupRecordException() {
    super();
  }

  public TaskDupRecordException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public TaskDupRecordException(String arg0) {
    super(arg0);
  }

  public TaskDupRecordException(Throwable arg1) {
    super(arg1);
  }

}
