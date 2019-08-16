package com.smate.core.base.utils.exception;

/**
 * 公用包相关异常
 * 
 * @author zk
 *
 */
public class PublicException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 7192708716203555697L;

  public PublicException() {
    super();
  }

  public PublicException(String arg0) {
    super(arg0);
  }

  public PublicException(Throwable arg1) {
    super(arg1);
  }

  public PublicException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
