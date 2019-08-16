package com.smate.web.prj.exception;

/*
 * 微信openid没有找到异常
 * 
 * @author zjh
 * 
 * @since 6.0.1
 * 
 * @version 6.0.1
 */
public class WechatNotBindException extends PrjException {

  /**
   * 
   */
  private static final long serialVersionUID = 1886850259320163916L;

  public WechatNotBindException() {
    super();
    // TODO Auto-generated constructor stub
  }

  public WechatNotBindException(String arg0) {
    super(arg0);
  }

  public WechatNotBindException(Throwable arg1) {
    super(arg1);
  }

  public WechatNotBindException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}

