package com.smate.center.batch.enums.pub;

/**
 * @author yamingd 期刊状态
 */
public interface JournalStatus {

  /**
   * 刚注册的.
   */
  static final int REGISTERED = 0;
  /**
   * 通过审核的.
   */
  static final int APPROVED = 1;
  /**
   * 已删除的.
   */
  static final int DELETED = 2;
}
