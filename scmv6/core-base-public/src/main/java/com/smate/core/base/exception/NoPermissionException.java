package com.smate.core.base.exception;

/**
 * 没有权限操作的异常
 * 
 * @author houchuanjie
 * @date 2018年3月19日 下午4:31:54
 */
public class NoPermissionException extends Exception {
  private static final long serialVersionUID = 4086425778984585247L;

  /**
   * 
   */
  public NoPermissionException() {
    super("您没有权限进行该项操作！");
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   * @param cause
   */
  public NoPermissionException(String message, Throwable cause) {
    super(message, cause);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   */
  public NoPermissionException(String message) {
    super(message);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param cause
   */
  public NoPermissionException(Throwable cause) {
    super(cause);
    // TODO 自动生成的构造函数存根
  }

}
