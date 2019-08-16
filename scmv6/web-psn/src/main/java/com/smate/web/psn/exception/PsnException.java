package com.smate.web.psn.exception;

/**
 * 人员信息异常-父类
 * 
 * @author Administrator
 *
 */
public class PsnException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7089293813133512917L;

  public PsnException() {
    super();
  }

  public PsnException(String arg0) {
    super(arg0);
  }

  public PsnException(Throwable arg1) {
    super(arg1);
  }

  public PsnException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
