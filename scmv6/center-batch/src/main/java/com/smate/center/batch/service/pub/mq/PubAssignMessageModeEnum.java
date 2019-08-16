package com.smate.center.batch.service.pub.mq;

/**
 * 指派模式.
 * 
 * @author yamingd
 * 
 */
public interface PubAssignMessageModeEnum {

  /**
   * 1(按单位).
   */
  static final int byIns = 1;
  /**
   * 2(按成果).
   */
  static final int byPub = 2;
  /**
   * 3(按人指派).
   */
  static final int byPsn = 3;
}
