package com.smate.core.base.utils.exception;

/**
 * 成果相关异常类
 * 
 * @author zk
 *
 */
public class PubException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -2369149131184566478L;

  public PubException() {
    super();
  }

  public PubException(String arg0) {
    super(arg0);
  }

  public PubException(Throwable arg1) {
    super(arg1);
  }

  public PubException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
