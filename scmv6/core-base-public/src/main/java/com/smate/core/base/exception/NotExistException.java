package com.smate.core.base.exception;

/**
 * 资源不存在的异常
 * 
 * @author houchuanjie
 * @date 2018年3月19日 下午5:13:10
 */
public class NotExistException extends Exception {
  private static final long serialVersionUID = 7089794096001194370L;

  /**
   * 
   */
  public NotExistException() {
    super("资源不存在！");
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   * @param cause
   */
  public NotExistException(String message, Throwable cause) {
    super(message, cause);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   */
  public NotExistException(String message) {
    super(message);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param cause
   */
  public NotExistException(Throwable cause) {
    super(cause);
    // TODO 自动生成的构造函数存根
  }

}
