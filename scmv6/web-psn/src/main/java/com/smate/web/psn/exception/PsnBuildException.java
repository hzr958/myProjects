package com.smate.web.psn.exception;

/**
 * 人员信息构造异常
 * 
 * @author zk
 *
 */
public class PsnBuildException extends Exception {

  private static final long serialVersionUID = -8690794316327102662L;

  public PsnBuildException() {
    super();
  }

  public PsnBuildException(String arg0) {
    super(arg0);
  }

  public PsnBuildException(Throwable arg1) {
    super(arg1);
  }

  public PsnBuildException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
