package com.smate.center.task.exception;

import com.smate.core.base.utils.exception.PubException;

public class PublicationNotFoundException extends PubException {

  /**
   * 异常-成果不存在
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   */
  private static final long serialVersionUID = -9141155536528304321L;

  public PublicationNotFoundException() {
    super();
  }

  public PublicationNotFoundException(String arg0) {
    super(arg0);
  }

  public PublicationNotFoundException(Throwable arg1) {
    super(arg1);
  }

  public PublicationNotFoundException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }


}
