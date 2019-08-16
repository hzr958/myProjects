package com.smate.center.job.framework.exception;

public class InterruptedJobException extends InterruptedException {

  public InterruptedJobException() {
    super();
  }

  public InterruptedJobException(String s) {
    super(s);
  }
}
