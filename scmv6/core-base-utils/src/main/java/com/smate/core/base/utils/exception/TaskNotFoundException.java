package com.smate.core.base.utils.exception;

/**
 * @author hzr 从数据库jason字段解析出的任务名为空.
 */
public class TaskNotFoundException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = -8040203647549867381L;

  public TaskNotFoundException(String msg, Long JobInstanceId) {

    super("任务名为空, msg=" + msg + ", JobInstanceId=" + JobInstanceId);

  }
}
