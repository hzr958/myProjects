package com.smate.center.batch.exception.pub;

import com.smate.core.base.utils.exception.PubException;

/**
 * 
 * 成果数据库 异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class PubDataException extends PubException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  public PubDataException() {
    super();
  }

  public PubDataException(String arg0) {
    super(arg0);
  }

  public PubDataException(Throwable arg1) {
    super(arg1);
  }

  public PubDataException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
