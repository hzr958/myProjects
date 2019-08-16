package com.smate.core.base.utils.exception;

/**
 * 成果拆分异常处理类.
 * 
 * @author mjg
 * 
 */
@SuppressWarnings("serial")
public class PubExpandException extends Exception {

  private Long pubId;// 成果ID.
  private String errorTask;// 错误日志.

  public PubExpandException() {
    super();
  }

  public PubExpandException(String arg0) {
    super(arg0);
  }

  public PubExpandException(Throwable arg1) {
    super(arg1);
  }

  public PubExpandException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public PubExpandException(Long pubId, String errorTask) {
    super();
    this.pubId = pubId;
    this.errorTask = errorTask;
  }

  public PubExpandException(Long pubId, String errorTask, Throwable cause) {
    super(cause);
    this.pubId = pubId;
    this.errorTask = errorTask;
  }

  public Long getPubId() {
    return pubId;
  }

  public String getErrorTask() {
    return errorTask;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setErrorTask(String errorTask) {
    this.errorTask = errorTask;
  }

}
