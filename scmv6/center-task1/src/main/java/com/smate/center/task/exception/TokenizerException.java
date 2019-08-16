package com.smate.center.task.exception;

/**
 * 分词器异常
 * 
 * @author zk
 *
 */
public class TokenizerException extends TaskException {

  private static final long serialVersionUID = -5756683085255467624L;

  public TokenizerException() {
    super();
  }

  public TokenizerException(String arg0) {
    super(arg0);
  }

  public TokenizerException(Throwable arg1) {
    super(arg1);
  }

  public TokenizerException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
