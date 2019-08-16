package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
public interface PubRolPsnStatRefreshEnum extends Serializable {
  /**
   * 更新整个单位的成果提交统计数.
   */
  public static Integer REFRESH_INS = 1;

  /**
   * 更新某单位某人员的成果提交统计数.
   */
  public static Integer REFRESH_INS_PSN = 2;

  /**
   * 批量更新某单位某人员的成果提交统计数.
   */
  public static Integer REFRESH_INS_PSN_BATCH = 3;

  /**
   * 更新某单位某成果的人员成果提交统计数.
   */
  public static Integer REFRESH_PUB_PSN = 4;
}
