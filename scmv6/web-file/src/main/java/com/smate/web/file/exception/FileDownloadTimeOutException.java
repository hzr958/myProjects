package com.smate.web.file.exception;

/**
 * 文件下载 缓存超时 SCM-16352
 * 
 * @author zzx
 *
 */
public class FileDownloadTimeOutException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FileDownloadTimeOutException() {
    super();
  }

  public FileDownloadTimeOutException(String arg0) {
    super(arg0);
  }

  public FileDownloadTimeOutException(Throwable arg1) {
    super(arg1);
  }

  public FileDownloadTimeOutException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
