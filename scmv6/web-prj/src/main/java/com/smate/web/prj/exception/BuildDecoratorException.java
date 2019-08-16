package com.smate.web.prj.exception;

/**
 * 构建装饰异常
 * 
 * @author zk
 *
 */
public class BuildDecoratorException extends PrjInfoBuildFactoryException {

  /**
   * 
   */
  private static final long serialVersionUID = -5586767445277135247L;

  public BuildDecoratorException() {
    super();
  }

  public BuildDecoratorException(String arg0) {
    super(arg0);
  }

  public BuildDecoratorException(Throwable arg1) {
    super(arg1);
  }

  public BuildDecoratorException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
