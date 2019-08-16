package com.smate.core.base.utils.exception;

/**
 * Xpath路径非法异常类.
 * 
 * @author yamingd
 */
public class InvalidXpathException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = 4010328641996201474L;

  /**
   * @param xpath 路径
   */
  public InvalidXpathException(final String xpath) {
    super("Xpath非法：" + xpath);
  }
}
