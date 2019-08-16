package com.smate.center.batch.constant;

/**
 * 单位成果，研究人员确认结果.
 * 
 * @author yamingd
 * 
 */
public interface PubConfirmResultEnum {

  /**
   * 0未确认.
   */
  static final int PENDING_CONFIRMATION = 0;
  /**
   * 1已确认.
   */
  static final int CONFIRMED = 1;
  /**
   * 2已拒绝.
   */
  static final int NOT_AUTHOR = 2;
}
