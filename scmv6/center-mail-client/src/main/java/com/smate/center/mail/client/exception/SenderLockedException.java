package com.smate.center.mail.client.exception;

/**
 * 发送账号被锁定异常
 * 
 * @author tsz
 *
 */
public class SenderLockedException extends MailClientException {

  /**
   * 
   */
  private static final long serialVersionUID = -7570297986830829086L;

  private static final String msg = "发送账号被锁定异常->";

  public SenderLockedException() {
    super(msg);
  }

  public SenderLockedException(String arg0) {
    super(msg + arg0);
  }

  public SenderLockedException(Throwable arg1) {
    super(msg, arg1);
  }

  public SenderLockedException(String arg0, Throwable arg1) {
    super(msg + arg0, arg1);
  }

}
