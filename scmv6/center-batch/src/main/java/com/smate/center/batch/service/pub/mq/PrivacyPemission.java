package com.smate.center.batch.service.pub.mq;

/**
 * @author oyh 隐私权限枚举类型
 */
public interface PrivacyPemission {

  /**
   * 任何人
   */
  static final Integer PEMISSION_ANYONE = 0;
  /**
   * 好友
   */
  static final Integer PEMISSION_FRD = 1;
  /**
   * 仅本人
   */

  static final Integer PEMISSION_SELF = 2;
}
