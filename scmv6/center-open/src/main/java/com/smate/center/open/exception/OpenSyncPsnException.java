package com.smate.center.open.exception;

/**
 * 
 * 人员同步-异常
 * 
 * @author ajb
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenSyncPsnException extends OpenException {

  private static final long serialVersionUID = -341502370292673097L;

  public OpenSyncPsnException() {
    super();
  }

  public OpenSyncPsnException(String arg0) {
    super(arg0);
  }

  public OpenSyncPsnException(Throwable arg1) {
    super(arg1);
  }

  public OpenSyncPsnException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
