package com.smate.core.base.utils.exception;

/**
 * 成果信息构建驱动异常
 * 
 * @author wsn
 * @date 2018年8月17日
 */
public class PubDriverNotFoundException extends Exception {

  private static final long serialVersionUID = 1178990754972452091L;

  public PubDriverNotFoundException(int typeId) {
    super("找不到成果信息构建驱动, typeId=" + typeId);
  }

}
