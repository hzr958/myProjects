package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
public interface PubRolSubmissionStatRefreshEnum extends Serializable {

  /**
   * 更新整个单位的成果提交统计数.
   */
  public static Integer REFRESH_INS = 1;

  /**
   * 更新某单位某人员的成果提交统计数.
   */
  public static Integer REFRESH_INS_PSN = 2;
}
