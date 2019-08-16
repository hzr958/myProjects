package com.smate.core.base.exception;

/**
 * 服务类执行异常
 * 
 * @author houchuanjie
 * @date 2018年1月3日 下午5:01:13
 */
public class ServiceException extends RuntimeException {
  private static final long serialVersionUID = -6385307610064944177L;

  public ServiceException() {
    super();
  }

  public ServiceException(String arg0) {
    super(arg0);
  }

  public ServiceException(Throwable arg1) {
    super(arg1);
  }

  public ServiceException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
