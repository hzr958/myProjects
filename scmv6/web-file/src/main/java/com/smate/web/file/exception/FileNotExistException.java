package com.smate.web.file.exception;

/**
 * 
 * 文件下载 文件不存在异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class FileNotExistException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  public FileNotExistException() {
    super();
  }

  public FileNotExistException(String arg0) {
    super(arg0);
  }

  public FileNotExistException(Throwable arg1) {
    super(arg1);
  }

  public FileNotExistException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
