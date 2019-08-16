package com.smate.center.batch.constant;

/**
 * 单位成果指派方式.
 * 
 * @author yamingd
 * 
 */
public interface PubAssignModeEnum {

  /**
   * 0指派界面手动指派.
   */
  static final int MANUALLY = 0;
  /**
   * 1后台自动.
   */
  static final int BACKGROUND_JOB = 1;
  /**
   * 2RO批准通过时建立.
   */
  static final int ASSIGN_ON_APPROVED = 2;

  /**
   * 3录入时指派.
   */
  static final int ASSIGN_ON_ENTER = 3;
  /**
   * 4导入时指派.
   */
  static final int ASSIGN_ON_IMPORT = 4;
}
