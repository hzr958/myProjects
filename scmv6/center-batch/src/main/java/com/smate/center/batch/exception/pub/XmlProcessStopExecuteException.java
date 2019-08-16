
package com.smate.center.batch.exception.pub;

/**
 * Xml处理过程终止执行异常.
 * 
 * @author yamingd
 */
public class XmlProcessStopExecuteException extends Exception {
  private static final long serialVersionUID = -8554832442387401993L;

  public XmlProcessStopExecuteException() {
    super();
  }

  public XmlProcessStopExecuteException(String arg0) {
    super(arg0);
  }

  public XmlProcessStopExecuteException(Throwable arg1) {
    super(arg1);
  }

  public XmlProcessStopExecuteException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
