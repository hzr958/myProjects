package com.smate.core.base.utils.exception;

/**
 * 枚举格式转换异常类
 * 
 * @author houchuanjie
 * @date 2018年3月2日 上午11:01:35
 */
public class EnumFormatException extends RuntimeException {

  private static final long serialVersionUID = -948511006670483507L;

  /**
   * 
   */
  public EnumFormatException() {
    super();
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public EnumFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   * @param cause
   */
  public EnumFormatException(String message, Throwable cause) {
    super(message, cause);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param message
   */
  public EnumFormatException(String message) {
    super(message);
    // TODO 自动生成的构造函数存根
  }

  /**
   * @param cause
   */
  public EnumFormatException(Throwable cause) {
    super(cause);
    // TODO 自动生成的构造函数存根
  }

}
