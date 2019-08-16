package com.smate.center.batch.constant;

/**
 * 
 * @author liqinghua
 * 
 */
public interface PublicationRolStatusEnum {

  // 1未批准/2已批准/3已删除

  /**
   * 未批准/待审核.
   */
  static final int PENDING_FOR_VERIFICATION = 1;
  /**
   * 已批准.
   */
  static final int APPROVED = 2;
  /**
   * 已删除.
   */
  static final int DELETED = 3;

  /**
   * 需要确认
   */
  static final int NEED_CONFIRM = 4;
}
