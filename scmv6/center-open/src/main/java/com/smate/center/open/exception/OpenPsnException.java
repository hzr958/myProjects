package com.smate.center.open.exception;

/**
 * 人员信息异常-父类
 * 
 * @author Administrator
 *
 */
public class OpenPsnException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7089293813133512917L;

  public OpenPsnException() {
    super();
  }

  public OpenPsnException(String arg0) {
    super(arg0);
  }

  public OpenPsnException(Throwable arg1) {
    super(arg1);
  }

  public OpenPsnException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
