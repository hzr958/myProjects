/**
 * <p>
 * Title: DaoMngException.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: OrderCenterV1.0 2007
 * </p>
 * <p>
 * Company: �����з����������޹�˾
 * </p>
 */

package com.smate.core.base.utils.exception;

/**
 * DAO层异常报告.
 * 
 * @author zb
 *
 */
public class SysDataException extends Exception {
  private static final long serialVersionUID = 8663831440217049102L;

  public SysDataException() {
    super();
  }

  public SysDataException(String arg0) {
    super(arg0);
  }

  public SysDataException(Throwable arg1) {
    super(arg1);
  }

  public SysDataException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
