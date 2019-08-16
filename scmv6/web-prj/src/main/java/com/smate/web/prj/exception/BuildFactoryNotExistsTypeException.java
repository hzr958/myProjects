package com.smate.web.prj.exception;

/**
 * 构建工厂类型不存在异常
 * 
 * @author zk
 *
 */
public class BuildFactoryNotExistsTypeException extends PrjInfoBuildFactoryException {

  /**
   * 
   */
  private static final long serialVersionUID = 6370312923090780441L;

  public BuildFactoryNotExistsTypeException() {
    super();
  }

  public BuildFactoryNotExistsTypeException(String arg0) {
    super(arg0);
  }

  public BuildFactoryNotExistsTypeException(Throwable arg1) {
    super(arg1);
  }

  public BuildFactoryNotExistsTypeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
