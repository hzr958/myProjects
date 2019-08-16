package com.smate.center.batch.exception.pub;

import com.smate.core.base.utils.exception.PubException;

/**
 * 
 * 成果dao异常
 * 
 * @author hzr
 * @since 6.0.1
 * @version 6.0.1
 */
public class PubDaoException extends PubException {


  /**
   * 
   */
  private static final long serialVersionUID = 4652269802970802203L;

  public PubDaoException() {
    super();
  }

  public PubDaoException(String arg0) {
    super(arg0);
  }

  public PubDaoException(Throwable arg1) {
    super(arg1);
  }

  public PubDaoException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
