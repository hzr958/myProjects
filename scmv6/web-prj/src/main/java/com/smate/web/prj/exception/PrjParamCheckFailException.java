package com.smate.web.prj.exception;

import com.smate.core.base.exception.ServiceException;

/**
 * 项目参数检查失败异常
 * 
 * @author SYL
 *
 */
public class PrjParamCheckFailException extends ServiceException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PrjParamCheckFailException() {
    super("项目参数检查失败");
  }

}
