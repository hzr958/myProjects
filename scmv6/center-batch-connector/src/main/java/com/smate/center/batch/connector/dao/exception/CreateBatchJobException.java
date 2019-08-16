package com.smate.center.batch.connector.dao.exception;

/**
 * 创建BatchJob对象异常
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class CreateBatchJobException extends Exception {

  private static final long serialVersionUID = -6867744428189133813L;

  public CreateBatchJobException() {
    super();
  }

  public CreateBatchJobException(String arg0) {
    super(arg0);
  }

  public CreateBatchJobException(Throwable arg1) {
    super(arg1);
  }

  public CreateBatchJobException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
