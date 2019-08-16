package com.smate.web.prj.exception;

public class PrjException extends Exception {

  /**
   * 项目异常
   * 
   * @author zk
   */
  private static final long serialVersionUID = 3258571376415512801L;

  public PrjException() {
    super();
  }

  public PrjException(String arg0) {
    super(arg0);
  }

  public PrjException(Throwable arg1) {
    super(arg1);
  }

  public PrjException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
