package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

/**
 * 成果提交状态枚举.
 * 
 * @author yamingd
 * 
 */
public interface PubRolSubmissionStatusEnum extends Serializable {

  /**
   * 准备中/未提交.
   */
  static final int IN_PREPARATION = 0;
  /**
   * 已提交/待审核.
   */
  static final int SUBMITTED = 1;
  /**
   * 已批准.
   */
  static final int APPROVED = 2;
  /**
   * 申请撤销.
   */
  static final int WITHDRAW_REQ = 3;
  /**
   * 已拒绝.
   */
  static final int REJECTED = 4;

  /**
   * 已撤销.
   */
  static final int WITHDRAWED = 5;
}
