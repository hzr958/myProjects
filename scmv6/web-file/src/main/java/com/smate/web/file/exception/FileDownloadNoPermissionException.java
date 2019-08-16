package com.smate.web.file.exception;

/**
 * 
 * 文件下载 没有下载权限异常
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 */
public class FileDownloadNoPermissionException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 7990853206826897517L;

  public FileDownloadNoPermissionException() {
    super();
  }

  public FileDownloadNoPermissionException(String arg0) {
    super(arg0);
  }

  public FileDownloadNoPermissionException(Throwable arg1) {
    super(arg1);
  }

  public FileDownloadNoPermissionException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
