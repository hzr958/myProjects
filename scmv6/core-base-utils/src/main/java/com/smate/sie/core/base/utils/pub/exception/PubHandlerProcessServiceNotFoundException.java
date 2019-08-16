package com.smate.sie.core.base.utils.pub.exception;

/**
 * PubHandlerProcessService生成驱动找不到异常.
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
public class PubHandlerProcessServiceNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 518415995876331707L;

  public PubHandlerProcessServiceNotFoundException(Integer typeId) {
    super("找不到PubHandlerProcessService生成驱动, typeId=" + typeId);
  }
}
